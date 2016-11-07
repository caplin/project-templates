package com.caplin.template.notification;

import com.caplin.datasource.notification.Notification;
import com.caplin.datasource.notification.NotificationProvider;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * This example publishes a notification to a predefined set of users every 10 Seconds.
 *
 * If the user is not logged in, then Transformer will cache the notification until the user logs in and then send it. This
 * only works in combination with the CPB_AlertsService. In addition the NotificaitonExtension Module can be used to
 * integrate notifications with a push notification service, email etc to send the notification outside the Caplin stack.
 *
 */
public class TemplateNotificationPublisher {
    private NotificationProvider notificationProvider;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private List<String> usernames = Arrays.asList("user1@caplin.com", "user2@caplin.com");

    public TemplateNotificationPublisher(NotificationProvider notificationProvider, Logger logger) {
        this.notificationProvider = notificationProvider;

        executor.scheduleAtFixedRate(this::publishNotifications, 10, 10, TimeUnit.SECONDS);
    }

    private void publishNotifications() {
        Notification notification = new Notification(UUID.randomUUID().toString());
        notification.setField("Message", "This is a notification published without a channel");

        usernames.forEach(user -> notificationProvider.publishNotification(user, notification));
    }
}
