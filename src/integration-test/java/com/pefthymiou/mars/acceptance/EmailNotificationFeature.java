package com.pefthymiou.mars.acceptance;

import com.pefthymiou.mars.MarsApplication;
import com.pefthymiou.mars.dsl.NotificationDsl;
import com.pefthymiou.mars.notifications.domain.actions.send.NotificationsScheduler;
import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import com.pefthymiou.mars.notifications.infrastructure.db.NotificationRepository;
import com.pefthymiou.mars.notifications.infrastructure.email.EmailDispatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import static com.pefthymiou.mars.dsl.NotificationDsl.ITNotificationBuilder.aNotification;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MarsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailNotificationFeature {

    private static final Timestamp NOW = Timestamp.from(Instant.now());
    private static final Timestamp FOURTEEN_MINS_AGO = Timestamp.from(Instant.now().minus(Duration.ofMinutes(14)));
    private static final Timestamp FOURTEEN_MINS_IN_THE_FUTURE = Timestamp.from(Instant.now().plus(Duration.ofMinutes(14)));
    private static final NotificationDsl.ITNotification IT_NOTIFICATION_1 = aNotification().withUnitTitle("House 1").withDueDate(NOW).build();
    private static final NotificationDsl.ITNotification IT_NOTIFICATION_2 = aNotification().withUnitTitle("House 2").withDueDate(FOURTEEN_MINS_AGO).build();
    private static final NotificationDsl.ITNotification IT_NOTIFICATION_3 = aNotification().withUnitTitle("House 3").withDueDate(FOURTEEN_MINS_IN_THE_FUTURE).build();

    private static final Notification NOTIFICATION_1 = new Notification(
            IT_NOTIFICATION_1.getEmail(),
            IT_NOTIFICATION_1.getUnitTitle(),
            IT_NOTIFICATION_1.getCheckIn(),
            IT_NOTIFICATION_1.getDueDate()
    );
    private static final Notification NOTIFICATION_2 = new Notification(
            IT_NOTIFICATION_2.getEmail(),
            IT_NOTIFICATION_2.getUnitTitle(),
            IT_NOTIFICATION_2.getCheckIn(),
            IT_NOTIFICATION_2.getDueDate()
    );
    private static final Notification NOTIFICATION_3 = new Notification(
            IT_NOTIFICATION_3.getEmail(),
            IT_NOTIFICATION_3.getUnitTitle(),
            IT_NOTIFICATION_3.getCheckIn(),
            IT_NOTIFICATION_3.getDueDate()
    );

    @Autowired
    private NotificationsScheduler notificationsScheduler;

    @MockBean
    private EmailDispatcher emailDispatcher;
    @MockBean
    private NotificationRepository notificationRepository;
    @Captor
    private ArgumentCaptor<Notification> captor;

    @Test
    public void sendEmailNotifications() {
        when(notificationRepository.findNotificationsDueBetween(any(), any())).thenReturn(asList(NOTIFICATION_1, NOTIFICATION_2, NOTIFICATION_3));

        notificationsScheduler.checkNotifications();

        verify(emailDispatcher, timeout(10000).times(3)).send(captor.capture());
        assertThat(captor.getAllValues()).containsExactlyInAnyOrder(NOTIFICATION_1, NOTIFICATION_2, NOTIFICATION_3);
    }
}
