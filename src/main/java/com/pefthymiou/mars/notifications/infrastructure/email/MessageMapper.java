package com.pefthymiou.mars.notifications.infrastructure.email;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MessageMapper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public SimpleMailMessage mapFrom(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getEmail());
        message.setSubject("Pack your bags for Mars!");
        message.setText(messageTextFrom(notification));
        return message;
    }

    private String messageTextFrom(Notification notification) {
        return new StringBuilder()
                .append("Get ready for your trip to the red planet, at ")
                .append(notification.getUnitTitle())
                .append(". Your check in is at ")
                .append(DATE_FORMAT.format(notification.getCheckIn()))
                .toString();
    }
}
