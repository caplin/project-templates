package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.trading.*;

import java.io.IOException;

/**
 * This is the main class for the trading adapter template.
 *
 * *** IMPORTANT ***
 * If this class is renamed, then the manifest section in the build file must be updated to reflect the name change
 */
public class TradingAdapterTemplate {

    private final TradingApplicationListener templateTradingApplicationListener;
    private final TemplateTradeListener templateTradeListener;

    public TradingAdapterTemplate(DataSource dataSource) throws IOException {

        templateTradeListener = new TemplateTradeListener(dataSource.getLogger());
        templateTradingApplicationListener = new TemplateTradingApplicationListener(templateTradeListener, dataSource.getLogger());

        new TradingProvider(templateTradingApplicationListener, dataSource);
    }

    public static void main(String[] args) throws IOException {
        DataSource dataSource = DataSource.fromArgs(args);

        new TradingAdapterTemplate(dataSource);

        dataSource.start();
    }

}
