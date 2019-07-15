package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.template.pricing.PricingTemplateDataProvider;

public class PricingAdapterTemplate {

    private final DataSource dataSource;

    public PricingAdapterTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        PricingTemplateDataProvider pricingTemplateDataProvider = new PricingTemplateDataProvider(dataSource);

        pricingTemplateDataProvider.initialise();
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSource.fromArgs(args);

        PricingAdapterTemplate pricingAdapterTemplate = new PricingAdapterTemplate(dataSource);
        pricingAdapterTemplate.initialise();

        dataSource.start();
    }
}
