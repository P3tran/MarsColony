package com.pefthymiou.mars.notifications;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import com.pefthymiou.mars.notifications.infrastructure.email.EmailDispatcher;
import com.pefthymiou.mars.notifications.infrastructure.email.MessageMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static com.pefthymiou.mars.base.builders.NotificationBuilder.aNotification;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EmailDispatcherShould {

    private static final Notification NOTIFICATION = aNotification().build();

    private EmailDispatcher emailDispatcher;

    @Mock
    private MessageMapper messageMapper;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private SimpleMailMessage MESSAGE;

    @Before
    public void setup() {
        initMocks(this);
        emailDispatcher = new EmailDispatcher(messageMapper, mailSender);
    }

    @Test
    public void sendAnEmailFromNotificationData() {
        when(messageMapper.mapFrom(NOTIFICATION)).thenReturn(MESSAGE);

        emailDispatcher.send(NOTIFICATION);

        verify(mailSender).send(MESSAGE);
    }
}
