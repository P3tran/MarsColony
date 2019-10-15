package com.pefthymiou.mars.base.builders;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NotificationBuilder {

    private long id = 1;
    private String email = "someone@example.com";
    private String unitTitle = "a title";
    private Timestamp checkIn = new Timestamp(ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());
    private Timestamp dueDate = new Timestamp(ZonedDateTime.of(2019, 6, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());

    public static NotificationBuilder aNotification() {
        return new NotificationBuilder();
    }

    public NotificationBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public NotificationBuilder withUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
        return this;
    }

    public NotificationBuilder withCheckIn(Timestamp checkIn) {
        this.checkIn = checkIn;
        return this;
    }

    public NotificationBuilder withDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Notification build() {
        return new Notification(
                email,
                unitTitle,
                checkIn,
                dueDate
        );
    }
}
