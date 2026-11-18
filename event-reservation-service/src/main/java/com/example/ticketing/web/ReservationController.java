package com.example.ticketing.web;

import com.example.ticketing.domain.reservation.Reservation;
import com.example.ticketing.service.ReservationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * HTTP API for exercising reservation creation and lifecycle transitions.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    public Reservation create(@RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(
                request.userId(), request.eventId(), request.pricingType(), request.basePrice());
    }

    @PostMapping("/pay")
    public Reservation pay(@RequestParam UUID reservationId) {
        return reservationService.confirmPayment(reservationId);
    }

    @PostMapping("/expire")
    public Reservation expire(@RequestParam UUID reservationId) {
        return reservationService.expireReservation(reservationId);
    }
}
