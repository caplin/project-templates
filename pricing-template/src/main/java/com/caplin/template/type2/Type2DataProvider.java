package com.caplin.template.type2;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.messaging.record.RecordType2Message;
import com.caplin.datasource.namespace.PrefixNamespace;
import com.caplin.datasource.publisher.ActivePublisher;
import com.caplin.datasource.publisher.DataProvider;
import com.caplin.datasource.publisher.DiscardEvent;
import com.caplin.datasource.publisher.RequestEvent;

public class Type2DataProvider {

	private static final int NUM_CONSTITUENTS = 50;

	private final DataSource dataSource;

	private ActivePublisher publisher;

	public Type2DataProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void initialise() {
		publisher = dataSource.createActivePublisher(new PrefixNamespace("/TEMPLATE/TYPE2/ALL"), new DataProvider() {
			@Override
			public void onRequest(RequestEvent requestEvent) {
				for (int i = 0; i < NUM_CONSTITUENTS; i++) {

					RecordType2Message type2Message = publisher.getMessageFactory().createRecordType2Message(
							requestEvent.getSubject(), "TYPE2_KEY", String.valueOf(i));

					if (i == 0) {
						type2Message.setImage(true);
						publisher.publishInitialMessage(type2Message);
					} else {
						publisher.publishToSubscribedPeers(type2Message);
					}
				}
			}

			@Override
			public void onDiscard(DiscardEvent discardEvent) {
				// Handle discard
			}
		});
	}
}
