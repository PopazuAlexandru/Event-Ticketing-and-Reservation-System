package com.example.ticketing.notification;

import com.example.ticketing.messaging.RabbitMqConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Entry point for the asynchronous notification microservice.
 */
@SpringBootApplication
@Import(RabbitMqConfiguration.class)
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}
