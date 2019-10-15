package com.pefthymiou.mars.notifications.domain.actions.send;

import com.pefthymiou.mars.notifications.domain.models.NotificationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class NotificationsScheduler {

    private static final String EVERY_15_MINUTES = "0 0/15 * * * *";

    private TaskExecutor notificationsTaskExecutor;
    private NotificationsProducer notificationsProducer;
    private NotificationsConsumer notificationsConsumer;

    @Autowired
    public NotificationsScheduler(TaskExecutor notificationsTaskExecutor, NotificationsProducer notificationsProducer, NotificationsConsumer notificationsConsumer) {
        this.notificationsTaskExecutor = notificationsTaskExecutor;
        this.notificationsProducer = notificationsProducer;
        this.notificationsConsumer = notificationsConsumer;
    }

    @Scheduled(cron = EVERY_15_MINUTES)
    public void checkNotifications() {
        BlockingQueue<NotificationData> notificationsQueue = new LinkedBlockingQueue<>();
        notificationsTaskExecutor.execute(notificationsProducer.produceTo(notificationsQueue));
        notificationsTaskExecutor.execute(notificationsConsumer.consumeFrom(notificationsQueue));
    }
}
