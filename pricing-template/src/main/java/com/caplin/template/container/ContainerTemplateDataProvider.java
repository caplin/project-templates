package com.caplin.template.container;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.messaging.container.ContainerMessage;
import com.caplin.datasource.messaging.record.GenericMessage;
import com.caplin.datasource.namespace.PrefixNamespace;
import com.caplin.datasource.publisher.ActivePublisher;
import com.caplin.datasource.publisher.DataProvider;
import com.caplin.datasource.publisher.DiscardEvent;
import com.caplin.datasource.publisher.RequestEvent;

public class ContainerTemplateDataProvider {

    private static final int NUM_CONSTITUENTS = 50;
    private final DataSource dataSource;

    private ActivePublisher containerPublisher;
    private ActivePublisher rowPublisher;

    public ContainerTemplateDataProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        containerPublisher = dataSource.createActivePublisher(new PrefixNamespace("/TEMPLATE/CONTAINER/ALL"), new DataProvider() {
            @Override
            public void onRequest(RequestEvent requestEvent) {
                ContainerMessage containerMessage = containerPublisher.getMessageFactory().createContainerMessage(requestEvent.getSubject());
                containerMessage.setDoNotAuthenticate(true);


                for (int i = 0; i < NUM_CONSTITUENTS; i++) {
                    containerMessage.addElement("/TEMPLATE/CONTAINERROW_" + i);
                }

                containerMessage.setImage(true);
                containerPublisher.publishInitialMessage(containerMessage);
            }

            @Override
            public void onDiscard(DiscardEvent discardEvent) {}
        });

        rowPublisher = dataSource.createActivePublisher(new PrefixNamespace("/TEMPLATE/CONTAINERROW_"), new DataProvider() {
            @Override
            public void onRequest(RequestEvent requestEvent) {
                GenericMessage updateMessage = rowPublisher.getMessageFactory().createGenericMessage(requestEvent.getSubject());
                updateMessage.setField("currentTime", String.valueOf(System.currentTimeMillis()));
                rowPublisher.publishInitialMessage(updateMessage);
            }

            @Override
            public void onDiscard(DiscardEvent discardEvent) {}
        });
    }
}
