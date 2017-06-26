package com.caplin.template;

import java.io.IOException;
import java.util.logging.Level;

import com.caplin.datasource.ConnectionListener;
import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;
import com.caplin.datasource.PeerStatusEvent;
import com.caplin.trading.TradingApplicationListener;
import com.caplin.trading.TradingProvider;

/**
 * This is the main class for the trading adapter template.
 *
 * *** IMPORTANT ***
 * If this class is renamed, then the manifest section in the build file must be updated to reflect the name change
 */
public class TradingAdapter implements ConnectionListener{

    private final DataSource dataSource;

    private final TradingApplicationListener templateTradingApplicationListener;
    private final TemplateTradeListener templateTradeListener;

    public TradingAdapter(DataSource dataSource) throws IOException {
        this.dataSource = dataSource;

        templateTradeListener = new TemplateTradeListener(dataSource.getLogger());
        templateTradingApplicationListener = new TemplateTradingApplicationListener(templateTradeListener, dataSource.getLogger());

        new TradingProvider(templateTradingApplicationListener, dataSource);
    }

    @Override
    public void onPeerStatus(PeerStatusEvent peerStatusEvent) {
        dataSource.getLogger().log(Level.INFO, "Peer status received: peer={0}, status={1}", new Object[] {peerStatusEvent.getPeer(), peerStatusEvent.getPeerStatus()});

    }

    public static void main(String[] args) throws IOException {
        DataSource dataSource = DataSourceFactory.createDataSource(args);

        new TradingAdapter(dataSource);

        dataSource.start();
    }

}
