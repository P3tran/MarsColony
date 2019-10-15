package com.pefthymiou.mars.notifications;

import com.pefthymiou.mars.notifications.domain.actions.send.NotificationsConsumer;
import com.pefthymiou.mars.notifications.domain.actions.send.NotificationsProducer;
import com.pefthymiou.mars.notifications.domain.actions.send.NotificationsScheduler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.BlockingQueue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NotificationsSchedulerShould {

    private NotificationsScheduler notificationsScheduler;

    @Mock
    private TaskExecutor notificationsTaskExecutor;
    @Mock
    private NotificationsProducer notificationsProducer;
    @Mock
    private NotificationsConsumer notificationsConsumer;
    @Mock
    private Runnable producerTask;
    @Mock
    private Runnable consumerTask;

    @Before
    public void setup() {
        initMocks(this);
        notificationsScheduler = new NotificationsScheduler(notificationsTaskExecutor, notificationsProducer, notificationsConsumer);
    }

    @Test
    public void startANotificationsProducer() {
        when(notificationsProducer.produceTo(any(BlockingQueue.class))).thenReturn(producerTask);

        notificationsScheduler.checkNotifications();

        verify(notificationsTaskExecutor).execute(producerTask);
    }

    @Test
    public void startANotificationsConsumer() {
        when(notificationsConsumer.consumeFrom(any(BlockingQueue.class))).thenReturn(consumerTask);

        notificationsScheduler.checkNotifications();

        verify(notificationsTaskExecutor).execute(consumerTask);
    }
}
