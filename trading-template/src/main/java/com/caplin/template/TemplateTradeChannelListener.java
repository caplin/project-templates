package com.caplin.template;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.caplin.trading.Trade;
import com.caplin.trading.TradeChannelListener;
import com.caplin.trading.TradeException;
import com.caplin.trading.TradeListener;

public class TemplateTradeChannelListener implements TradeChannelListener {
    private TradeListener tradeListener;
    private final Logger logger;

    public TemplateTradeChannelListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void tradeCreated(Trade trade) throws TradeException {
        logger.log(Level.INFO, "Trade created");
        tradeListener = new TemplateTradeListener(logger);
        trade.setTradeListener(tradeListener);
    }

    @Override
    public void tradeClosed(Trade trade) {
        logger.log(Level.INFO, "Trade closed");
    }
}
