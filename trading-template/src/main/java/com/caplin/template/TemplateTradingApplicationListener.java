package com.caplin.template;

import com.caplin.trading.*;

import java.util.logging.Level;
import java.util.logging.Logger;

class TemplateTradingApplicationListener implements TradingApplicationListener {

    private final TradeListener tradeListener;
    private final Logger logger;

    public TemplateTradingApplicationListener(TradeListener tradeListener, Logger logger) {
        this.tradeListener = tradeListener;
        this.logger = logger;
    }

    @Override
    public void channelCreated(TradeChannel channel) throws TradeException {
        logger.log(Level.INFO, "Trade channel created: " + channel.toString());
        channel.setTradeChannelListener(new TemplateTradeChannelListener(tradeListener, logger));
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
