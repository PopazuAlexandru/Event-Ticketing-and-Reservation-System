package com.example.ticketing.messaging;

/**
 * RabbitMQ exchange, queue, and routing-key names used by all services.
 */
public final class TicketingMessaging {
    public static final String EXCHANGE = "ticket.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String EXPIRATION_QUEUE = "expiration.queue";
    public static final String RESERVATION_CREATED = "RESERVATION_CREATED";
    public static final String PAYMENT_COMPLETED = "PAYMENT_COMPLETED";
    public static final String RESERVATION_EXPIRED = "RESERVATION_EXPIRED";
    public static final String RESERVATION_CREATED_ROUTING_KEY = "reservation.created";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.completed";
    public static final String RESERVATION_EXPIRED_ROUTING_KEY = "reservation.expired";

    private TicketingMessaging() {
    }
}
