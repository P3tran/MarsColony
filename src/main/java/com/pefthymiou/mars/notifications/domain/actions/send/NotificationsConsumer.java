package com.pefthymiou.mars.notifications.domain.actions.send;

import com.pefthymiou.mars.notifications.domain.models.NotificationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotificationsConsumer {

    @Autowired
    private NotificationsDispatcher notificationsDispatcher;

    public Runnable consumeFrom(BlockingQueue<NotificationData> notificationsQueue) {
        return () -> {
            try {
                while (true) {
                    NotificationData notificationData = notificationsQueue.take();

                    if (notificationData.isPoisonPill())
                        break;

                    notificationsDispatcher.send(notificationData.getNotification());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }
}
