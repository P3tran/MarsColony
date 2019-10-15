package com.pefthymiou.mars.notifications;

import com.pefthymiou.mars.base.builders.NotificationBuilder;
import com.pefthymiou.mars.notifications.domain.actions.send.BookingNotificationService;
import com.pefthymiou.mars.notifications.domain.actions.send.NotificationMapper;
import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import com.pefthymiou.mars.notifications.infrastructure.db.NotificationRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BookingNotificationServiceShould {

    private static final Notification NOTIFICATION = NotificationBuilder.aNotification().build();
    private static final String EMAIL = "someone@example.com";
    private static final String TITLE = "a title";
    private static final Timestamp CHECK_IN = new Timestamp(ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());

    private BookingNotificationService bookingNotificationService;

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationMapper notificationMapper;

    @Before
    public void setup() {
        initMocks(this);
        bookingNotificationService = new BookingNotificationService(
                notificationRepository,
                notificationMapper
        );
    }

    @Test
    public void createANewNotification() {
        when(notificationMapper.mapFrom(EMAIL, TITLE, CHECK_IN)).thenReturn(NOTIFICATION);

        bookingNotificationService.createWith(EMAIL, TITLE, CHECK_IN);

        verify(notificationRepository).save(NOTIFICATION);
    }
}
