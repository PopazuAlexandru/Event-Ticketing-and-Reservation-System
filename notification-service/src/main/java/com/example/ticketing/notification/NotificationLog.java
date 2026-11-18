package com.example.ticketing.notification;

import java.time.Instant;
import java.util.UUID;

/**
 * In-memory record of a simulated user notification.
 */
public record NotificationLog(
        UUID id,
        String eventType,
        String userId,
        UUID eventId,
        UUID reservationId,
        String message,
        Instant receivedAt,
        String deliveryChannel) {
}
