package com.pefthymiou.mars.notifications.domain.actions.send;

import com.pefthymiou.mars.notifications.domain.models.BookingNotificationData;
import com.pefthymiou.mars.notifications.domain.models.NotificationData;
import com.pefthymiou.mars.notifications.domain.models.PoisonPill;
import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import com.pefthymiou.mars.notifications.infrastructure.db.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotificationsProducer {

    @Autowired
    private NotificationRepository notificationRepository;

    public Runnable produceTo(BlockingQueue<NotificationData> notificationsQueue) {
        return () -> {
            try {
                Timestamp fifteenMinutesAgo = Timestamp.from(Instant.now().minus(Duration.ofMinutes(15)));
                Timestamp fifteenMinutesInTheFuture = Timestamp.from(Instant.now().plus(Duration.ofMinutes(15)));

                Iterable<Notification> notifications = notificationRepository.findNotificationsDueBetween(fifteenMinutesAgo, fifteenMinutesInTheFuture);

                for (Notification notification : notifications)
                    notificationsQueue.put(new BookingNotificationData(notification));

                notificationsQueue.put(new PoisonPill());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }
}
