package com.caplin.template;

import com.caplin.trading.Trade;
import com.caplin.trading.TradeChannelListener;
import com.caplin.trading.TradeException;
import com.caplin.trading.TradeListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TemplateTradeChannelListener implements TradeChannelListener {
    private final TradeListener tradeListener;
    private final Logger logger;

    public TemplateTradeChannelListener(TradeListener tradeListener, Logger logger) {
        this.tradeListener = tradeListener;
        this.logger = logger;
    }

    @Override
    public void tradeCreated(Trade trade) throws TradeException {
        logger.log(Level.INFO, "Trade created");
        trade.setTradeListener(tradeListener);
    }

    @Override
    public void tradeClosed(Trade trade) {
        logger.log(Level.INFO, "Trade closed");
    }
}
