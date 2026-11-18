package com.example.ticketing.domain.reservation;

/**
 * Terminal state for an unpaid reservation whose payment window elapsed.
 */
public class ExpiredState implements ReservationState {
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
        return ReservationStatus.EXPIRED;
    }

    private IllegalStateException invalidTransition(String action) {
        return new IllegalStateException("Cannot " + action + " an expired reservation");
    }
}
