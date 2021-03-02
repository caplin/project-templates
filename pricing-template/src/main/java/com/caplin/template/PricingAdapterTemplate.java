package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.template.channel.ChannelTemplate;
import com.caplin.template.channel.JsonChannelTemplate;
import com.caplin.template.container.ContainerTemplateDataProvider;
import com.caplin.template.pricing.PricingTemplateDataProvider;
import com.caplin.template.type2.Type2DataProvider;

public class PricingAdapterTemplate {

    private final DataSource dataSource;

    public PricingAdapterTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        PricingTemplateDataProvider pricingTemplateDataProvider = new PricingTemplateDataProvider(dataSource);
        ContainerTemplateDataProvider containerTemplateDataProvider = new ContainerTemplateDataProvider(dataSource);
        ChannelTemplate channelTemplate = new ChannelTemplate(dataSource);
        JsonChannelTemplate jsonChannelTemplate = new JsonChannelTemplate(dataSource);
        Type2DataProvider type2DataProvider = new Type2DataProvider(dataSource);

        pricingTemplateDataProvider.initialise();
        containerTemplateDataProvider.initialise();
        channelTemplate.initialise();
        jsonChannelTemplate.initialise();
        type2DataProvider.initialise();
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSource.fromArgs(args);

        PricingAdapterTemplate pricingAdapterTemplate = new PricingAdapterTemplate(dataSource);
        pricingAdapterTemplate.initialise();

        dataSource.start();
    }
}
