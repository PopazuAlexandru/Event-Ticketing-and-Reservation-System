package com.example.ticketing.messaging;

import java.time.Instant;
import java.util.UUID;

/**
 * Versionable message contract shared by ticketing services.
 */
public record TicketingEvent(
        String eventType,
        UUID eventId,
        UUID reservationId,
        String userId,
        Instant occurredAt,
        String message) {
}
