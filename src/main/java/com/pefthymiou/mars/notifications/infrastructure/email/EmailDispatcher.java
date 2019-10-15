package com.pefthymiou.mars.notifications.infrastructure.email;

import com.pefthymiou.mars.notifications.domain.actions.send.NotificationsDispatcher;
import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailDispatcher implements NotificationsDispatcher {

    private MessageMapper messageMapper;
    private JavaMailSender mailSender;

    @Autowired
    public EmailDispatcher(MessageMapper messageMapper, JavaMailSender mailSender) {
        this.messageMapper = messageMapper;
        this.mailSender = mailSender;
    }

    public void send(Notification notification) {
        SimpleMailMessage message = messageMapper.mapFrom(notification);
        mailSender.send(message);
    }
}
