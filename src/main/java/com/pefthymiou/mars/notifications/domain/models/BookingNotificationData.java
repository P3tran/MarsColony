package com.pefthymiou.mars.notifications.domain.models;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;

public class BookingNotificationData implements NotificationData {

    private Notification notification;

    public BookingNotificationData(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }

    @Override
    public boolean isPoisonPill() {
        return false;
    }
}
