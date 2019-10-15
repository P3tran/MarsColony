package com.pefthymiou.mars.notifications;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import com.pefthymiou.mars.notifications.infrastructure.email.MessageMapper;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.pefthymiou.mars.base.builders.NotificationBuilder.aNotification;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class MessageMapperShould {

    private static final Timestamp CHECK_IN = new Timestamp(ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0,ZoneId.of("UTC")).toInstant().toEpochMilli());
    private static final Notification NOTIFICATION = aNotification().withUnitTitle("Green Valley comfort house").withCheckIn(CHECK_IN).build();

    private MessageMapper messageMapper = new MessageMapper();

    @Test
    public void mapAMessageFromANotification() {
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(NOTIFICATION.getEmail());
        expectedMessage.setSubject("Pack your bags for Mars!");
        expectedMessage.setText("Get ready for your trip to the red planet, at Green Valley comfort house. Your check in is at 05-07-2019");

        SimpleMailMessage actualMessage = messageMapper.mapFrom(NOTIFICATION);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
