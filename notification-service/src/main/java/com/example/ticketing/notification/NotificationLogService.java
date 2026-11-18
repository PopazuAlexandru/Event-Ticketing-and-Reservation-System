package com.example.ticketing.notification;

import com.example.ticketing.messaging.TicketingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simulates delivery and retains notification records for testing.
 */
@Service
public class NotificationLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationLogService.class);
    private final CopyOnWriteArrayList<NotificationLog> logs = new CopyOnWriteArrayList<>();

    public void record(TicketingEvent event, String deliveryChannel) {
        NotificationLog log = new NotificationLog(
                UUID.randomUUID(),
                event.eventType(),
                event.userId(),
                event.eventId(),
                event.reservationId(),
                event.message(),
                Instant.now(),
                deliveryChannel);
        logs.add(log);
        LOGGER.info("Simulated {} notification to user {}: {} ({})",
                deliveryChannel, event.userId(), event.message(), event.reservationId());
    }

    public List<NotificationLog> getLogs() {
        return List.copyOf(logs);
    }
}
