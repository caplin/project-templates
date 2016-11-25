package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;
import com.caplin.template.channel.ChannelTemplate;
import com.caplin.template.container.ContainerTemplateDataProvider;
import com.caplin.template.pricing.PricingTemplateDataProvider;

public class PricingAdapterTemplate {

    private final DataSource dataSource;

    public PricingAdapterTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        PricingTemplateDataProvider pricingTemplateDataProvider = new PricingTemplateDataProvider(dataSource);
        ContainerTemplateDataProvider containerTemplateDataProvider = new ContainerTemplateDataProvider(dataSource);
        ChannelTemplate channelTemplate = new ChannelTemplate(dataSource);

        pricingTemplateDataProvider.initialise();
        containerTemplateDataProvider.initialise();
        channelTemplate.initialise();
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSourceFactory.createDataSource(args);

        PricingAdapterTemplate pricingAdapterTemplate = new PricingAdapterTemplate(dataSource);
        pricingAdapterTemplate.initialise();

        dataSource.start();
    }
}
