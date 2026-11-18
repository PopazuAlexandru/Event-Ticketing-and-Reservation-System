package com.example.ticketing.domain.reservation;

/**
 * Defines lifecycle operations that a reservation state may allow.
 */
public interface ReservationState {
    void confirmPayment(Reservation reservation);

    void cancel(Reservation reservation);

    void expire(Reservation reservation);

    ReservationStatus status();
}
