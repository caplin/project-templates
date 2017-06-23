package com.caplin.template.pricing;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.namespace.PrefixNamespace;
import com.caplin.datasource.publisher.ActivePublisher;
import com.caplin.datasource.publisher.DataProvider;
import com.caplin.datasource.publisher.DiscardEvent;
import com.caplin.datasource.publisher.RequestEvent;

public class PricingDataProvider implements DataProvider {

    private final DataSource dataSource;
    private ActivePublisher publisher;

    public PricingDataProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        publisher = dataSource.createActivePublisher(new PrefixNamespace("/FX/"), this);
    }

    @Override
    public void onRequest(RequestEvent requestEvent) {
        System.out.println("Received request for " + requestEvent.getSubject());
    }

    @Override
    public void onDiscard(DiscardEvent discardEvent) {
        System.out.println("Received discard for " + discardEvent.getSubject());
    }
}
