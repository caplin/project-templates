package com.caplin.template;

import com.caplin.trading.*;

import java.util.logging.Logger;

public class TemplateTradeListener implements TradeListener {
    private Logger logger;

    public TemplateTradeListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void receiveEvent(TradeEvent event) throws TradeException {
        logger.info("Received trade event: " + event.toString());

        switch (event.getType()) {
            case "Open":
                String price = event.getField("Price");
                logger.info("Received trade for price " + (price != null ? price : "(null)"));
                TradeEvent confirm = event.getTrade().createEvent("Confirm");
                event.getTrade().sendEvent(confirm);
                break;
        }
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
