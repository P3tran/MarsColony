package com.pefthymiou.mars.notifications.domain.models;


import com.pefthymiou.mars.notifications.infrastructure.db.Notification;

public interface NotificationData {

    Notification getNotification();

    boolean isPoisonPill();
}
