package com.example.ticketing;

import com.example.ticketing.domain.event.Event;
import com.example.ticketing.domain.event.EventBuilder;
import com.example.ticketing.domain.reservation.Reservation;
import com.example.ticketing.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Runs a console demonstration of the builder, strategy, state, and observer patterns.
 */
@Component
public class PoCTestRunner implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(PoCTestRunner.class);
    private final ReservationService reservationService;

    public PoCTestRunner(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void run(String... args) {
        Event event = new EventBuilder()
                .title("Spring Boot Live")
                .location("Main Hall")
                .dateTime(LocalDateTime.now().plusDays(30))
                .totalCapacity(100)
                .build();
        reservationService.registerEvent(event);
        LOGGER.info("Registered demo event {} ({})", event.getTitle(), event.getId());

        Reservation paidReservation = reservationService.createReservation(
                "user-001", event.getId(), "EARLY_BIRD", 50.00);
        LOGGER.info("Created reservation {} at discounted price {}", paidReservation.getId(),
                paidReservation.getPrice());
        reservationService.confirmPayment(paidReservation.getId());

        Reservation expiringReservation = reservationService.createReservation(
                "user-002", event.getId(), "GROUP_DISCOUNT", 50.00);
        LOGGER.info("Created reservation {} at group price {}", expiringReservation.getId(),
                expiringReservation.getPrice());
        reservationService.expireReservation(expiringReservation.getId());
    }
}
