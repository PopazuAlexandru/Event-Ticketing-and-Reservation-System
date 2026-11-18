package com.example.ticketing.domain.reservation;

/**
 * State of a reservation awaiting payment.
 */
public class PendingState implements ReservationState {
    @Override
    public void confirmPayment(Reservation reservation) {
        reservation.setStateBehavior(new ConfirmedState());
    }

    @Override
    public void cancel(Reservation reservation) {
        reservation.setStateBehavior(new CancelledState());
    }

    @Override
    public void expire(Reservation reservation) {
        reservation.setStateBehavior(new ExpiredState());
    }

    @Override
    public ReservationStatus status() {
        return ReservationStatus.PENDING;
    }
}
