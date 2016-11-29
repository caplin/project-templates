package com.caplin.template;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
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
    private final Map<UUID, ObservableItem<TodoItem>> uuidToTodoItemObservable = new HashMap<>();

    private ActivePublisher containerPublisher;
    private ActivePublisher rowPublisher;

    private static class ObservableItem<T> {
        private ItemObserver<T> observer;
        private T latestValue;

        void onNext(final T latestValue) {
            this.latestValue = latestValue;
            final ItemObserver<T> observer = this.observer;
            if (observer != null) {
                observer.accept(latestValue);
            }
        }

        ObservableItemSubscription subscribe(final ItemObserver<T> observer) {
            this.observer = observer;
            observer.accept(latestValue);
            return () -> this.observer = null;
        }
    }

    private interface ItemObserver<T> extends Consumer<T> {}

    private interface ObservableItemSubscription {
        void unsubscribe();
    }

    TodoContainerDataProvider(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    void initialise() {
        containerPublisher = dataSource.createActivePublisher(new RegexNamespace(CONTAINER_SUBJECT_REGEX), new DataProvider() {

            private final Map<String, TodoSubscription> userToTodoSubscription = new HashMap<>();

            @Override
            public void onRequest(final RequestEvent requestEvent) {

                final ContainerMessage initialMessage = containerPublisher.getMessageFactory().createContainerMessage(requestEvent.getSubject());
                initialMessage.setDoNotAuthenticate(true);
                initialMessage.setImage(true);
                containerPublisher.publishInitialMessage(initialMessage);

                userToTodoSubscription.computeIfAbsent(CONTAINER_SUBJECT_PATTERN.matcher(requestEvent.getSubject()).group(1), username ->
                        todoService.subscribeForItems(username, todoItem ->
                        {
                            // Handle insert or update
                            final UUID uuid = todoItem.getId();
                            final ObservableItem<TodoItem> prevObservableItem =
                                    uuidToTodoItemObservable.get(uuid);
                            final ObservableItem<TodoItem> observableItem;
                            if (prevObservableItem == null) {
                                observableItem = new ObservableItem<>();
                            } else {
                                observableItem = prevObservableItem;
                            }
                            observableItem.onNext(todoItem);

                            if (prevObservableItem == null) {
                                final ContainerMessage insertMessage = containerPublisher.getMessageFactory().createContainerMessage(requestEvent.getSubject());
                                insertMessage.addElement("/PRIVATE/" + username + "/TODO/ITEM/" + uuid);
                                containerPublisher.publishToSubscribedPeers(insertMessage);
                            }
                        }, uuid ->
                        {
                            // Handle remove
                            uuidToTodoItemObservable.remove(uuid);
                            final ContainerMessage removeMessage = containerPublisher.getMessageFactory().createContainerMessage(requestEvent.getSubject());
                            removeMessage.removeElement("/PRIVATE/" + username + "/TODO/ITEM/" + uuid);
                            containerPublisher.publishToSubscribedPeers(removeMessage);
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

            private final Map<String, ObservableItemSubscription> subjectToTodoItemObservableSubscription = new HashMap<>();

            @Override
            public void onRequest(final RequestEvent requestEvent) {
                final String subject = requestEvent.getSubject();
                final UUID uuid = UUID.fromString(CONTAINER_ROW_SUBJECT_PATTERN.matcher(subject).group(2));
                final ObservableItem<TodoItem> observableItem = uuidToTodoItemObservable.get(uuid);

                // Because we aren't using a lock on the todoItemObservable's latestValue, we will end up publishing it
                // twice even if it's not changed - but it's not very expensive so doesn't matter too much.
                // It's better than the alternative which is to miss an update.
                sendUpdate(subject, observableItem.latestValue, true);

                subjectToTodoItemObservableSubscription.computeIfAbsent(subject, itemUuid ->
                        observableItem.subscribe(todoItem ->
                                sendUpdate(subject, observableItem.latestValue, false)));
            }

            @Override
            public void onDiscard(final DiscardEvent discardEvent) {
                // Handle last peer discard, remove and cancel the future
                subjectToTodoItemObservableSubscription.remove(discardEvent.getSubject()).unsubscribe();
            }

            private void sendUpdate(final String subject, final TodoItem todoItem, final boolean isInitialMessage) {
                final GenericMessage message = rowPublisher.getMessageFactory().createGenericMessage(subject);
                message.setField("value", todoItem.getValue());
                message.setField("priority", String.valueOf(todoItem.getPriority()));
                message.setImage(isInitialMessage);
                if (isInitialMessage) {
                    rowPublisher.publishInitialMessage(message);
                } else {
                    rowPublisher.publishToSubscribedPeers(message);
                }
            }
        });
    }
}
