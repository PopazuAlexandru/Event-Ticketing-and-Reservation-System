package com.example.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.example.ticketing.messaging.RabbitMqConfiguration;

/**
 * Application entry point for the event ticketing proof of concept.
 */
@SpringBootApplication
@Import(RabbitMqConfiguration.class)
public class EventTicketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventTicketingApplication.class, args);
    }
}
