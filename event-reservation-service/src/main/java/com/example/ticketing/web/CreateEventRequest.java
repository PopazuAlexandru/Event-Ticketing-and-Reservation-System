package com.example.ticketing.web;

import java.time.LocalDateTime;

/**
 * Request body for creating an event.
 */
public record CreateEventRequest(
        String title,
        String location,
        LocalDateTime dateTime,
        int totalCapacity) {
}
