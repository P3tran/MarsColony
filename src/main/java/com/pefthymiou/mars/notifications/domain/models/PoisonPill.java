package com.pefthymiou.mars.notifications.domain.models;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;

public class PoisonPill implements NotificationData {

    @Override
    public Notification getNotification() {
        throw new RuntimeException("Poison pill does not carry any notification data");
    }

    @Override
    public boolean isPoisonPill() {
        return true;
    }
}
