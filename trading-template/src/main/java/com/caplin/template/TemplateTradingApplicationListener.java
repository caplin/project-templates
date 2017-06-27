package com.caplin.template;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.caplin.datasource.DataSource;
import com.caplin.trading.*;

class TemplateTradingApplicationListener implements TradingApplicationListener {

    private final Logger logger;
    private TemplateTradeChannelListener tradeChannelListener;

    public TemplateTradingApplicationListener(DataSource dataSource) throws IOException {
        this.logger = dataSource.getLogger();

        /**
         * This is how the trading library is initialised. We pass in a DataSource instance,
         * which is used to receive requests, discards and contributions from Liberator, and
         * a TradingApplicationListener for the trading library to notify of events. It is not
         * necessary to keep a reference to this object after you instantiate it.
         */
        new TradingProvider(this, dataSource);
    }

    @Override
    public void channelCreated(TradeChannel channel) throws TradeException {
        logger.log(Level.INFO, "Trade channel created: " + channel.toString());
        tradeChannelListener = new TemplateTradeChannelListener(logger);
        channel.setTradeChannelListener(tradeChannelListener);
    }

    @Override
    public void channelClosed(TradeChannel channel) {
        logger.log(Level.INFO, "Trade channel closed: " + channel.toString());
    }

    @Override
    public void blotterChannelCreated(BlotterChannel blotterChannel) {
        //This is an outdated API and it is recommended to use the Caplin Blotter API instead
    }

    @Override
    public void blotterChannelClosed(BlotterChannel blotterChannel) {
        //This is an outdated API and it is recommended to use the Caplin Blotter API instead
    }

    @Override
    public void peerUp(int peerIndex) {
        logger.log(Level.INFO, "Peer up: " + peerIndex);
    }

    @Override
    public void peerDown(int peerIndex) {
        logger.log(Level.INFO, "Peer down: " + peerIndex);
    }
}
