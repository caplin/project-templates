package com.caplin.template;

import java.util.*;
import java.util.regex.Pattern;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.messaging.container.ContainerMessage;
import com.caplin.datasource.messaging.record.GenericMessage;
import com.caplin.datasource.namespace.RegexNamespace;
import com.caplin.datasource.publisher.ActivePublisher;
import com.caplin.datasource.publisher.DataProvider;
import com.caplin.datasource.publisher.DiscardEvent;
import com.caplin.datasource.publisher.RequestEvent;
import com.caplin.template.service.TodoItem;
import com.caplin.template.service.TodoService;
import com.caplin.template.service.TodoSubscription;
import com.caplin.template.util.Observable;
import com.caplin.template.util.ObservableSubscription;

class TodoContainerDataProvider {

    // Matches a typical username mapped in via Liberator
    private static final String VALID_USERNAME_REGEX = "([0-9a-zA-Z@.-])";
    private static final String ITEM_UUID_REGEX = "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})";

    private static final String CONTAINER_SUBJECT_REGEX = "^/PRIVATE/" + VALID_USERNAME_REGEX + "/TODO/CONTAINER$";
    private static final Pattern CONTAINER_SUBJECT_PATTERN = Pattern.compile(CONTAINER_SUBJECT_REGEX);

    private static final String CONTAINER_ROW_SUBJECT_REGEX = "^/PRIVATE/" + VALID_USERNAME_REGEX + "/TODO/ITEM/" + ITEM_UUID_REGEX + "$";
    private static final Pattern CONTAINER_ROW_SUBJECT_PATTERN = Pattern.compile(CONTAINER_ROW_SUBJECT_REGEX);

    private final DataSource dataSource;
    private final TodoService todoService = new TodoService();
    private final Map<UUID, Observable<TodoItem>> uuidToTodoItemObservable = new HashMap<>();

    private ActivePublisher containerPublisher;
    private ActivePublisher rowPublisher;

    TodoContainerDataProvider(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    void initialise() {
        containerPublisher = dataSource.createActivePublisher(new RegexNamespace(CONTAINER_SUBJECT_REGEX), new DataProvider() {

            private final Map<String, TodoSubscription> userToTodoSubscription = new HashMap<>();

            @Override
            public void onRequest(final RequestEvent requestEvent) {
                final String username = CONTAINER_SUBJECT_PATTERN.matcher(requestEvent.getSubject()).group(1);

                final Set<UUID> uuids = new LinkedHashSet<>();
                synchronized (uuids) {
                    // Publish an empty container immediately to let Liberator know we're accepting the request.
                    final ContainerMessage initialMessage = containerPublisher.getMessageFactory().createContainerMessage(requestEvent.getSubject());
                    initialMessage.setDoNotAuthenticate(true);
                    uuids.forEach(uuid -> initialMessage.addElement("/PRIVATE/" + username + "/TODO/ITEM/" + uuid));
                    initialMessage.setImage(true);
                    containerPublisher.publishInitialMessage(initialMessage);
                }

                userToTodoSubscription.computeIfAbsent(username, usernameKey ->
                        todoService.subscribeForItems(usernameKey, todoItem ->
                        {
                            // Handle insert or update
                            final UUID uuid = todoItem.getId();
                            final Observable<TodoItem> prevObservableItem = uuidToTodoItemObservable.get(uuid);
                            final Observable<TodoItem> observableItem = prevObservableItem == null ? new Observable<>() : prevObservableItem;
                            observableItem.onNext(todoItem);

                            if (prevObservableItem == null) {
                                synchronized (uuids) {
                                    uuids.add(uuid);
                                    final ContainerMessage insertMessage = containerPublisher.getMessageFactory().createContainerMessage(requestEvent.getSubject());
                                    insertMessage.addElement("/PRIVATE/" + usernameKey + "/TODO/ITEM/" + uuid);
                                    containerPublisher.publishToSubscribedPeers(insertMessage);
                                }
                            }
                        }, uuid ->
                        {
                            // Handle remove
                            uuidToTodoItemObservable.remove(uuid);
                            synchronized (uuids) {
                                uuids.remove(uuid);
                                final ContainerMessage removeMessage = containerPublisher.getMessageFactory().createContainerMessage(requestEvent.getSubject());
                                removeMessage.removeElement("/PRIVATE/" + usernameKey + "/TODO/ITEM/" + uuid);
                                containerPublisher.publishToSubscribedPeers(removeMessage);
                            }
                        }));
            }

            @Override
            public void onDiscard(final DiscardEvent discardEvent) {
                // Handle discard
                final String username = CONTAINER_SUBJECT_PATTERN.matcher(discardEvent.getSubject()).group(1);
                userToTodoSubscription.get(username).unsubscribe();
            }
        });

        rowPublisher = dataSource.createActivePublisher(new RegexNamespace(CONTAINER_ROW_SUBJECT_REGEX), new DataProvider() {

            private final Map<String, ObservableSubscription> subjectToTodoItemObservableSubscription = new HashMap<>();

            @Override
            public void onRequest(final RequestEvent requestEvent) {
                final String subject = requestEvent.getSubject();
                final UUID uuid = UUID.fromString(CONTAINER_ROW_SUBJECT_PATTERN.matcher(subject).group(2));
                final Observable<TodoItem> observableItem = uuidToTodoItemObservable.get(uuid);

                synchronized (observableItem) {
                    // Publish an initial image to the new peer, with the current value
                    final GenericMessage initialMessage = rowPublisher.getMessageFactory().createGenericMessage(subject);
                    final TodoItem latestValue = observableItem.getLatestValue();
                    initialMessage.setField("value", latestValue.getValue());
                    initialMessage.setField("priority", String.valueOf(latestValue.getPriority()));
                    initialMessage.setImage(true);
                    rowPublisher.publishInitialMessage(initialMessage);
                }

                subjectToTodoItemObservableSubscription.computeIfAbsent(subject, itemUuid ->
                        observableItem.subscribe(todoItem -> {
                            synchronized (observableItem) {
                                // Publish any updates to all subscribed peers
                                final GenericMessage updateMessage = rowPublisher.getMessageFactory().createGenericMessage(subject);
                                updateMessage.setField("value", todoItem.getValue());
                                updateMessage.setField("priority", String.valueOf(todoItem.getPriority()));
                                rowPublisher.publishToSubscribedPeers(updateMessage);
                            }
                        }));
            }

            @Override
            public void onDiscard(final DiscardEvent discardEvent) {
                // Handle last peer discard, remove and cancel the future
                subjectToTodoItemObservableSubscription.remove(discardEvent.getSubject()).unsubscribe();
            }
        });
    }
}
