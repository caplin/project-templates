package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;

public class TodoTemplateAdapter {

    private final DataSource dataSource;

    public TodoTemplateAdapter(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        final TodoContainerDataProvider todoContainerDataProvider = new TodoContainerDataProvider(dataSource);

        todoContainerDataProvider.initialise();
    }

    public static void main(final String[] args) {
        final DataSource dataSource = DataSourceFactory.createDataSource(args);

        final TodoTemplateAdapter todoTemplateAdapter = new TodoTemplateAdapter(dataSource);
        todoTemplateAdapter.initialise();

        dataSource.start();
    }
}
