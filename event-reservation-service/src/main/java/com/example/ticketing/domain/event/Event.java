package com.example.ticketing.domain.event;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * An event for which users can reserve tickets.
 */
public class Event {
    private final UUID id;
    private final String title;
    private final String location;
    private final LocalDateTime dateTime;
    private final int totalCapacity;

    Event(UUID id, String title, String location, LocalDateTime dateTime, int totalCapacity) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.location = Objects.requireNonNull(location, "location must not be null");
        this.dateTime = Objects.requireNonNull(dateTime, "dateTime must not be null");
        if (totalCapacity <= 0) {
            throw new IllegalArgumentException("totalCapacity must be greater than zero");
        }
        this.totalCapacity = totalCapacity;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }
}
