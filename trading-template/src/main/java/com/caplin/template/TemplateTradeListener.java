package com.caplin.template;

import java.util.logging.Logger;

import com.caplin.trading.*;

public class TemplateTradeListener implements TradeListener {
    private Logger logger;

    public TemplateTradeListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void receiveEvent(TradeEvent event) throws TradeException {
        logger.info("Received trade event: " + event.toString());
    }

    @Override
    public void receiveInvalidTransitionEvent(InvalidTransitionEvent event) {
        logger.warning("Received invalid trade transition: " + event.toString());
    }

    @Override
    public void receiveInvalidFieldsEvent(InvalidFieldsEvent event) {
        logger.warning("Received invalid fields: " + event.toString());
    }
}
