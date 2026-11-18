package com.example.ticketing.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes reservation lifecycle events to the shared topic exchange.
 */
@Component
public class TicketingEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public TicketingEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(TicketingEvent event) {
        String routingKey = switch (event.eventType()) {
            case TicketingMessaging.RESERVATION_CREATED -> TicketingMessaging.RESERVATION_CREATED_ROUTING_KEY;
            case TicketingMessaging.PAYMENT_COMPLETED -> TicketingMessaging.PAYMENT_COMPLETED_ROUTING_KEY;
            case TicketingMessaging.RESERVATION_EXPIRED -> TicketingMessaging.RESERVATION_EXPIRED_ROUTING_KEY;
            default -> throw new IllegalArgumentException("Unsupported event type: " + event.eventType());
        };
        rabbitTemplate.convertAndSend(TicketingMessaging.EXCHANGE, routingKey, event);
    }
}
