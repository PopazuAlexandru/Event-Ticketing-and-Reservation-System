package com.example.ticketing.domain.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A ticket reservation and its current lifecycle state.
 */
public class Reservation {
    private final UUID id;
    private final String userId;
    private final UUID eventId;
    private final LocalDateTime createdAt;
    private ReservationStatus state;
    @JsonIgnore
    private transient ReservationState stateBehavior;
    private final String ticketCategory;
    private final double price;

    public Reservation(String userId, UUID eventId, String ticketCategory, double price) {
        this.id = UUID.randomUUID();
        this.userId = requireText(userId, "userId");
        this.eventId = Objects.requireNonNull(eventId, "eventId must not be null");
        this.createdAt = LocalDateTime.now();
        this.ticketCategory = requireText(ticketCategory, "ticketCategory");
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("price must be a finite, non-negative value");
        }
        this.price = price;
        setStateBehavior(new PendingState());
    }

    public synchronized void confirmPayment() {
        currentState().confirmPayment(this);
    }

    public synchronized void cancel() {
        currentState().cancel(this);
    }

    public synchronized void expire() {
        currentState().expire(this);
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationStatus getState() {
        return state;
    }

    public String getTicketCategory() {
        return ticketCategory;
    }

    public double getPrice() {
        return price;
    }

    @JsonIgnore
    public ReservationState getStateBehavior() {
        return currentState();
    }

    public synchronized void setStateBehavior(ReservationState stateBehavior) {
        this.stateBehavior = Objects.requireNonNull(stateBehavior, "stateBehavior must not be null");
        this.state = stateBehavior.status();
    }

    private ReservationState currentState() {
        if (stateBehavior == null) {
            throw new IllegalStateException("Reservation state behavior is not initialized");
        }
        return stateBehavior;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}
