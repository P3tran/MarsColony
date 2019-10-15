package com.pefthymiou.mars.notifications.domain.actions.send;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;

public interface NotificationsDispatcher {

    void send(Notification notification);
}
