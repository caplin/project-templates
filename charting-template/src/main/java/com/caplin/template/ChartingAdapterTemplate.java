package com.caplin.template;

import com.caplin.charting.ChartingProvider;
import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;
import com.caplin.template.charting.TemplateChartDataProvider;

public class ChartingAdapterTemplate {

    private final DataSource dataSource;

    public ChartingAdapterTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        TemplateChartDataProvider templateChartDataProvider = new TemplateChartDataProvider();
        new ChartingProvider(dataSource, templateChartDataProvider);
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSourceFactory.createDataSource(args);

        ChartingAdapterTemplate chartingAdapterTemplate = new ChartingAdapterTemplate(dataSource);
        chartingAdapterTemplate.initialise();

        dataSource.start();
    }
}
