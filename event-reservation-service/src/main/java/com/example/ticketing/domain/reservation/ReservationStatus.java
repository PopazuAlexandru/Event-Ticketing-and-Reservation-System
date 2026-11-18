package com.example.ticketing.domain.reservation;

/**
 * Stable, serializable lifecycle status for a reservation.
 */
public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    EXPIRED
}
