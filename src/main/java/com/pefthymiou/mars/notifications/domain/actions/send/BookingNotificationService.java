package com.pefthymiou.mars.notifications.domain.actions.send;

import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import com.pefthymiou.mars.notifications.infrastructure.db.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class BookingNotificationService implements NotificationsService {

    private NotificationRepository notificationRepository;
    private NotificationMapper notificationMapper;

    @Autowired
    public BookingNotificationService(
            NotificationRepository notificationRepository,
            NotificationMapper notificationMapper
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void createWith(String email, String title, Timestamp checkIn) {
        Notification notification = notificationMapper.mapFrom(email, title, checkIn);
        notificationRepository.save(notification);
    }
}
