package com.example.ticketing.domain.reservation;

/**
 * Terminal state for a cancelled reservation.
 */
public class CancelledState implements ReservationState {
    @Override
    public void confirmPayment(Reservation reservation) {
        throw invalidTransition("confirm payment");
    }

    @Override
    public void cancel(Reservation reservation) {
        throw invalidTransition("cancel");
    }

    @Override
    public void expire(Reservation reservation) {
        throw invalidTransition("expire");
    }

    @Override
    public ReservationStatus status() {
        return ReservationStatus.CANCELLED;
    }

    private IllegalStateException invalidTransition(String action) {
        return new IllegalStateException("Cannot " + action + " a cancelled reservation");
    }
}
