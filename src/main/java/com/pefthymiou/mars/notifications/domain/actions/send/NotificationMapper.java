package com.pefthymiou.mars.notifications.domain.actions.send;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;

@Component
public class NotificationMapper {

    public Notification mapFrom(String email, String unitTitle, Timestamp checkIn) {
        return new Notification(
                email,
                unitTitle,
                checkIn,
                calculateDueDateFor(checkIn)
        );
    }

    private Timestamp calculateDueDateFor(Timestamp checkIn) {
        return Timestamp.from(checkIn.toInstant().minus(Duration.ofDays(30)));
    }
}
