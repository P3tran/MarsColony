package com.pefthymiou.mars.notifications.domain.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class NotificationsConfiguration {

    @Bean
    public TaskExecutor notificationsTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
