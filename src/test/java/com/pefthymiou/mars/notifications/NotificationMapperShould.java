package com.pefthymiou.mars.notifications;

import com.pefthymiou.mars.base.builders.NotificationBuilder;
import com.pefthymiou.mars.notifications.domain.actions.send.NotificationMapper;
import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.pefthymiou.mars.base.builders.NotificationBuilder.aNotification;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationMapperShould {

    private static final Timestamp CHECK_IN_DATE_1 = new Timestamp(ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());
    private static final Timestamp THIRTY_DAYS_BEFORE_CHECK_IN_DATE_1 = new Timestamp(ZonedDateTime.of(2019, 6, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());

    private static final Timestamp CHECK_IN_DATE_2 = new Timestamp(ZonedDateTime.of(2019, 1, 4, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());
    private static final Timestamp THIRTY_DAYS_BEFORE_CHECK_IN_DATE_2 = new Timestamp(ZonedDateTime.of(2018, 12, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());

    private static final Timestamp CHECK_IN_DATE_3 = new Timestamp(ZonedDateTime.of(2019, 3, 11, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());
    private static final Timestamp THIRTY_DAYS_BEFORE_CHECK_IN_DATE_3 = new Timestamp(ZonedDateTime.of(2019, 2, 9, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());

    private static final String EMAIL_1 = "someone1@example.com";
    private static final String EMAIL_2 = "someone2@example.com";
    private static final String EMAIL_3 = "someone3@example.com";

    private static final String UNIT_TITLE_1 = "a title";
    private static final String UNIT_TITLE_2 = "a title";
    private static final String UNIT_TITLE_3 = "a title";

    private NotificationMapper notificationMapper = new NotificationMapper();

    private Notification expectedNotification1 = aNotification()
            .withEmail(EMAIL_1)
            .withUnitTitle(UNIT_TITLE_1)
            .withCheckIn(CHECK_IN_DATE_1)
            .withDueDate(THIRTY_DAYS_BEFORE_CHECK_IN_DATE_1)
            .build();
    private Notification expectedNotification2 = aNotification()
            .withEmail(EMAIL_2)
            .withUnitTitle(UNIT_TITLE_2)
            .withCheckIn(CHECK_IN_DATE_2)
            .withDueDate(THIRTY_DAYS_BEFORE_CHECK_IN_DATE_2)
            .build();
    private Notification expectedNotification3 = aNotification()
            .withEmail(EMAIL_3)
            .withUnitTitle(UNIT_TITLE_3)
            .withCheckIn(CHECK_IN_DATE_3)
            .withDueDate(THIRTY_DAYS_BEFORE_CHECK_IN_DATE_3)
            .build();

    @Test
    public void createANotification() {
        Notification actualNotification1 = notificationMapper.mapFrom(EMAIL_1, UNIT_TITLE_1, CHECK_IN_DATE_1);
        Notification actualNotification2 = notificationMapper.mapFrom(EMAIL_2, UNIT_TITLE_2, CHECK_IN_DATE_2);
        Notification actualNotification3 = notificationMapper.mapFrom(EMAIL_3, UNIT_TITLE_3, CHECK_IN_DATE_3);

        assertThat(actualNotification1).isEqualToIgnoringGivenFields(expectedNotification1, "id");
        assertThat(actualNotification2).isEqualToIgnoringGivenFields(expectedNotification2, "id");
        assertThat(actualNotification3).isEqualToIgnoringGivenFields(expectedNotification3, "id");
    }
}