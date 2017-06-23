package com.caplin.template;

import java.util.logging.Level;

import com.caplin.datasource.ConnectionListener;
import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;
import com.caplin.datasource.PeerStatusEvent;
import com.caplin.template.pricing.PricingDataProvider;

public class PricingAdapter implements ConnectionListener{

    private final DataSource dataSource;

    public PricingAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        PricingDataProvider pricingDataProvider = new PricingDataProvider(dataSource);
        pricingDataProvider.initialise();
    }

    @Override
    public void onPeerStatus(PeerStatusEvent peerStatusEvent) {
        dataSource.getLogger().log(Level.INFO, "Peer status received: peer={0}, status={1}", new Object[] {peerStatusEvent.getPeer(), peerStatusEvent.getPeerStatus()});

    }

    public static void main(String[] args) {
        DataSource dataSource = DataSourceFactory.createDataSource(args);

        PricingAdapter pricingAdapter = new PricingAdapter(dataSource);
        pricingAdapter.initialise();

        dataSource.start();
    }
}
