package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;

public class TodoAdapterTemplate {

    private final DataSource dataSource;

    public TodoAdapterTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialise() {
        final TodoContainerDataProvider todoContainerDataProvider = new TodoContainerDataProvider(dataSource);

        todoContainerDataProvider.initialise();
    }

    public static void main(final String[] args) {
        final DataSource dataSource = DataSourceFactory.createDataSource(args);

        final TodoAdapterTemplate todoAdapterTemplate = new TodoAdapterTemplate(dataSource);
        todoAdapterTemplate.initialise();

        dataSource.start();
    }
}
