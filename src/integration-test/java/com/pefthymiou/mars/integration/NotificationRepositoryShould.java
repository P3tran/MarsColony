package com.pefthymiou.mars.integration;

import com.pefthymiou.mars.dsl.MarsDsl;
import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import com.pefthymiou.mars.notifications.infrastructure.db.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;

import static com.pefthymiou.mars.dsl.NotificationDsl.*;
import static com.pefthymiou.mars.dsl.NotificationDsl.ITNotificationBuilder.aNotification;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NotificationRepositoryShould {

    private static final Timestamp NOW = Timestamp.from(Instant.now());
    private static final Timestamp SIXTEEN_MINS_AGO = Timestamp.from(Instant.now().minus(Duration.ofMinutes(16)));
    private static final Timestamp FOURTEEN_MINS_AGO = Timestamp.from(Instant.now().minus(Duration.ofMinutes(14)));
    private static final Timestamp SIXTEEN_MINS_IN_THE_FUTURE = Timestamp.from(Instant.now().plus(Duration.ofMinutes(16)));
    private static final Timestamp FOURTEEN_MINS_IN_THE_FUTURE = Timestamp.from(Instant.now().plus(Duration.ofMinutes(14)));
    private static final ITNotification NOTIFICATION_1 = aNotification().withUnitTitle("House 1").withDueDate(NOW).build();
    private static final ITNotification NOTIFICATION_2 = aNotification().withUnitTitle("House 2").withDueDate(SIXTEEN_MINS_AGO).build();
    private static final ITNotification NOTIFICATION_3 = aNotification().withUnitTitle("House 3").withDueDate(FOURTEEN_MINS_AGO).build();
    private static final ITNotification NOTIFICATION_4 = aNotification().withUnitTitle("House 4").withDueDate(SIXTEEN_MINS_IN_THE_FUTURE).build();
    private static final ITNotification NOTIFICATION_5 = aNotification().withUnitTitle("House 5").withDueDate(FOURTEEN_MINS_IN_THE_FUTURE).build();

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private MarsDsl marsDsl;

    @Test
    public void fetchNotificationDueBetweenFifteenMinutesAgoAndFifteenMinutesInTheFuture() {
        marsDsl.create(NOTIFICATION_1);
        marsDsl.create(NOTIFICATION_2);
        marsDsl.create(NOTIFICATION_3);
        marsDsl.create(NOTIFICATION_4);
        marsDsl.create(NOTIFICATION_5);
        Timestamp fifteenMinutesAgo = Timestamp.from(Instant.now().minus(Duration.ofMinutes(15)));
        Timestamp fifteenMinutesInTheFuture = Timestamp.from(Instant.now().plus(Duration.ofMinutes(15)));

        Iterable<Notification> actualNotifications = notificationRepository.findNotificationsDueBetween(fifteenMinutesAgo, fifteenMinutesInTheFuture);

        Iterator<Notification> iterator = actualNotifications.iterator();
        assertThat(iterator.next().getUnitTitle()).isEqualTo(NOTIFICATION_1.getUnitTitle());
        assertThat(iterator.next().getUnitTitle()).isEqualTo(NOTIFICATION_3.getUnitTitle());
        assertThat(iterator.next().getUnitTitle()).isEqualTo(NOTIFICATION_5.getUnitTitle());
        assertThat(iterator.hasNext()).isFalse();
    }
}
