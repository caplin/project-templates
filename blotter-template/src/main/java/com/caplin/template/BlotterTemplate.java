package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.blotter.BlotterConfiguration;
import com.caplin.datasource.blotter.BlotterProvider;
import com.caplin.template.blotter.BlotterUpdateGenerator;

public class BlotterTemplate
{

    public static final String BLOTTER_NAME = "BlotterTemplate";
    public static final String CHANNEL_NAMESPACE = "/TEMPLATE/BLOTTER/%u/CHANNEL";
    public static final String ITEM_NAMESPACE = "/TEMPLATE/BLOTTER/%u/ITEM/%i";
    public static final String SUB_CONTAINER_NAMESPACE = "/TEMPLATE/BLOTTER/%u/SUBCONTAINER/%i";

    public BlotterTemplate(DataSource dataSource)
    {
        BlotterConfiguration configuration = new BlotterConfiguration(BLOTTER_NAME, CHANNEL_NAMESPACE, ITEM_NAMESPACE, SUB_CONTAINER_NAMESPACE);
        configuration.setSubcontainerMaxDepth(2);
        configuration.setChannelUsingImageFlag(false);
        configuration.setAutoPermit(true);

        new BlotterProvider(dataSource, configuration, new BlotterUpdateGenerator(dataSource.getLogger()));
    }

    public static void main(String[] args) throws Exception
    {
        DataSource dataSource = DataSource.fromArgs(args);

        new BlotterTemplate(dataSource);

        dataSource.start();
    }
}
