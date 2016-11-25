package com.caplin.template.container;

import java.util.logging.Level;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.messaging.container.ContainerMessage;
import com.caplin.datasource.messaging.record.RecordType1Message;
import com.caplin.datasource.messaging.record.RecordType2Message;
import com.caplin.datasource.namespace.PrefixNamespace;
import com.caplin.datasource.publisher.ActivePublisher;
import com.caplin.datasource.publisher.DataProvider;
import com.caplin.datasource.publisher.DiscardEvent;
import com.caplin.datasource.publisher.RequestEvent;

public class ContainerTemplateDataProvider implements DataProvider {

    private static final int NUM_CONSTITUENTS = 50;
    private final DataSource dataSource;

    private ActivePublisher publisher;

    public ContainerTemplateDataProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        publisher = dataSource.createActivePublisher(new PrefixNamespace("/TEMPLATE/CONTAINER"), this);
    }

    @Override
    public void onRequest(RequestEvent requestEvent) {
        if (requestEvent.getSubject().equals("/TEMPLATE/CONTAINER/ALL")) {
            ContainerMessage containerMessage = publisher.getMessageFactory().createContainerMessage(requestEvent.getSubject());
            containerMessage.setDoNotAuthenticate(true);


            for (int i = 0; i < NUM_CONSTITUENTS; i++) {
                containerMessage.addElement("/TEMPLATE/CONTAINERROW_" + i);
            }

            containerMessage.setImage(true);
            publisher.publishInitialMessage(containerMessage);
        } else if (requestEvent.getSubject().startsWith("/TEMPLATE/CONTAINERROW")) {
            RecordType1Message message = publisher.getMessageFactory().createRecordType1Message(requestEvent.getSubject());

            message.setImage(true);
            publisher.publishInitialMessage(message);
        } else if (requestEvent.getSubject().equals("/TEMPLATE/TYPE2/ALL")) {
            for (int i = 0; i < NUM_CONSTITUENTS; i++) {

                RecordType2Message type2Message = publisher.getMessageFactory().createRecordType2Message(requestEvent.getSubject(), "TYPE2_KEY", String.valueOf(i));

                if (i == 0) {
                    type2Message.setImage(true);
                    publisher.publishInitialMessage(type2Message);
                } else {
                    publisher.publishToSubscribedPeers(type2Message);
                }
            }
        } else {
            dataSource.getLogger().log(Level.INFO, "Unknown subject " + requestEvent.getSubject());
        }

    }

    @Override
    public void onDiscard(DiscardEvent discardEvent) {}
}
