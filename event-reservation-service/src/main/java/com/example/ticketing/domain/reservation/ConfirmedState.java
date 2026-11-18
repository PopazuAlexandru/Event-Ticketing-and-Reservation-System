package com.example.ticketing.domain.reservation;

/**
 * State of a reservation with successful payment.
 */
public class ConfirmedState implements ReservationState {
    @Override
    public void confirmPayment(Reservation reservation) {
        throw invalidTransition("confirm payment");
    }

    @Override
    public void cancel(Reservation reservation) {
        reservation.setStateBehavior(new CancelledState());
    }

    @Override
    public void expire(Reservation reservation) {
        throw invalidTransition("expire");
    }

    @Override
    public ReservationStatus status() {
        return ReservationStatus.CONFIRMED;
    }

    private IllegalStateException invalidTransition(String action) {
        return new IllegalStateException("Cannot " + action + " a confirmed reservation");
    }
}
