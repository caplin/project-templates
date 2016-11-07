package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;
import com.caplin.datasource.notification.NotificationApplicationListener;
import com.caplin.datasource.notification.NotificationConfiguration;
import com.caplin.datasource.notification.NotificationProvider;
import com.caplin.template.notification.TemplateNotificationListener;
import com.caplin.template.notification.TemplateNotificationPublisher;

public class NotificationTemplate {

    private final DataSource dataSource;

    public NotificationTemplate(DataSource dataSource) {
        this.dataSource = dataSource;

        NotificationApplicationListener notificationListener = new TemplateNotificationListener(this.dataSource.getLogger());
        NotificationConfiguration config = new NotificationConfiguration("NotificationTemplate");

        NotificationProvider notificationProvider = new NotificationProvider(this.dataSource, config, notificationListener);
        new TemplateNotificationPublisher(notificationProvider, this.dataSource.getLogger());
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSourceFactory.createDataSource(args);

        new NotificationTemplate(dataSource);

        dataSource.start();
    }

}
