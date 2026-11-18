package com.example.ticketing.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Fluent builder for constructing validated events step by step.
 */
public class EventBuilder {
    private UUID id = UUID.randomUUID();
    private String title;
    private String location;
    private LocalDateTime dateTime;
    private int totalCapacity;

    public EventBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public EventBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EventBuilder location(String location) {
        this.location = location;
        return this;
    }

    public EventBuilder dateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public EventBuilder totalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
        return this;
    }

    public Event build() {
        if (title == null || title.isBlank()) {
            throw new IllegalStateException("title must be provided");
        }
        if (location == null || location.isBlank()) {
            throw new IllegalStateException("location must be provided");
        }
        if (dateTime == null) {
            throw new IllegalStateException("dateTime must be provided");
        }
        return new Event(id, title, location, dateTime, totalCapacity);
    }
}
