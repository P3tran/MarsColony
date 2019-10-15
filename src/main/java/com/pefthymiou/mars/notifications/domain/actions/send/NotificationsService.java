package com.pefthymiou.mars.notifications.domain.actions.send;

import java.sql.Timestamp;

public interface NotificationsService {

    void createWith(String email, String title, Timestamp checkIn);
}
