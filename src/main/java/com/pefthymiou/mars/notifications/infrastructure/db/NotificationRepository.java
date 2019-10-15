package com.pefthymiou.mars.notifications.infrastructure.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    @Query("Select n from Notification n where n.dueDate > :lowerLimit and n.dueDate < :upperLimit")
    Iterable<Notification> findNotificationsDueBetween(@Param("lowerLimit") Timestamp lowerLimit, @Param("upperLimit") Timestamp upperLimit);
}
