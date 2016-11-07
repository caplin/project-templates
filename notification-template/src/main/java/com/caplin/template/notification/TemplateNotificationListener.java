package com.caplin.template.notification;

import com.caplin.datasource.notification.Notification;
import com.caplin.datasource.notification.NotificationApplicationListener;
import com.caplin.datasource.notification.NotificationChannel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Send a notification to logged in users every 5 seconds
 */
public class TemplateNotificationListener implements NotificationApplicationListener {

    private AtomicInteger notificationID = new AtomicInteger(0);
    private ConcurrentHashMap<String, NotificationChannel> activeChannels = new ConcurrentHashMap<>();
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Logger logger;

    public TemplateNotificationListener(Logger logger) {
        this.logger = logger;
        executor.scheduleAtFixedRate(this::sendNotificationToActiveUsers, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void notificationChannelOpened(NotificationChannel notificationChannel) {
        logger.log(Level.INFO, "Notification Channel opened. {}", new Object[]{notificationChannel});
        activeChannels.put(notificationChannel.getSubject(), notificationChannel);
    }

    @Override
    public void notificationChannelClosed(NotificationChannel notificationChannel) {
        logger.log(Level.INFO, "Notification Channel closed. {}", new Object[]{notificationChannel});
        activeChannels.remove(notificationChannel.getSubject(), notificationChannel);
    }

    private void sendNotificationToActiveUsers() {
        if (activeChannels.size() > 0)  logger.log(Level.INFO, "Sending notification to active users");
        Notification notification = new Notification(String.valueOf(notificationID.getAndIncrement()));
        notification.setField("Message", "Some notification text");

        activeChannels.forEach((subject, channel) -> channel.sendNotification(notification));
    }
}
