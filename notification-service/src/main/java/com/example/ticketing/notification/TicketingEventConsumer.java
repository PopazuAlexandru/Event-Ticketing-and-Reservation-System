package com.example.ticketing.notification;

import com.example.ticketing.messaging.TicketingEvent;
import com.example.ticketing.messaging.TicketingMessaging;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes event and payment notifications, and reservation expiry messages.
 */
@Component
public class TicketingEventConsumer {
    private final NotificationLogService notificationLogService;

    public TicketingEventConsumer(NotificationLogService notificationLogService) {
        this.notificationLogService = notificationLogService;
    }

    @RabbitListener(queues = TicketingMessaging.NOTIFICATION_QUEUE)
    public void consumeNotification(TicketingEvent event) {
        notificationLogService.record(event, "EMAIL");
    }

    @RabbitListener(queues = TicketingMessaging.EXPIRATION_QUEUE)
    public void consumeExpiration(TicketingEvent event) {
        notificationLogService.record(event, "SMS");
    }
}
