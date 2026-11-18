package com.example.ticketing;

import com.example.ticketing.domain.event.Event;
import com.example.ticketing.domain.event.EventBuilder;
import com.example.ticketing.domain.pricing.EarlyBirdPricing;
import com.example.ticketing.domain.pricing.GroupDiscountPricing;
import com.example.ticketing.domain.pricing.StandardPricing;
import com.example.ticketing.domain.pricing.TicketCategory;
import com.example.ticketing.domain.reservation.Reservation;
import com.example.ticketing.domain.reservation.ReservationStatus;
import com.example.ticketing.messaging.TicketingEventPublisher;
import com.example.ticketing.messaging.TicketingMessaging;
import com.example.ticketing.service.ReservationService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DesignPatternsTest {

    @Test
    void eventBuilderCreatesAnEventWithAllProvidedProperties() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(2);
        Event event = new EventBuilder()
                .title("Conference")
                .location("Auditorium")
                .dateTime(dateTime)
                .totalCapacity(25)
                .build();

        assertEquals("Conference", event.getTitle());
        assertEquals("Auditorium", event.getLocation());
        assertEquals(dateTime, event.getDateTime());
        assertEquals(25, event.getTotalCapacity());
    }

    @Test
    void reservationStateTransitionsAndRejectsInvalidTransitions() {
        Reservation reservation = new Reservation("user-1", java.util.UUID.randomUUID(), "STANDARD", 20);
        reservation.confirmPayment();

        assertEquals(ReservationStatus.CONFIRMED, reservation.getState());
        assertThrows(IllegalStateException.class, reservation::expire);
    }

    @Test
    void pricingStrategiesApplyExpectedDiscounts() {
        assertEquals(100.0, new StandardPricing().calculatePrice(100.0));
        assertEquals(80.0, new EarlyBirdPricing().calculatePrice(100.0));
        assertEquals(85.0, new GroupDiscountPricing().calculatePrice(100.0));

        TicketCategory category = new TicketCategory("EARLY_BIRD", 100.0, new EarlyBirdPricing());
        assertEquals(80.0, category.getPrice());
        category.setPricingStrategy(new StandardPricing());
        assertEquals(100.0, category.getPrice());
    }

    @Test
    void reservationServicePublishesLifecycleEventsAndReleasesCapacityOnExpiry() {
        TicketingEventPublisher publisher = mock(TicketingEventPublisher.class);
        ReservationService service = new ReservationService(publisher);
        Event event = new EventBuilder()
                .title("Small Event")
                .location("Room 1")
                .dateTime(LocalDateTime.now().plusDays(1))
                .totalCapacity(1)
                .build();
        service.registerEvent(event);

        Reservation expiring = service.createReservation("user-1", event.getId(), "STANDARD", 10);
        assertThrows(IllegalStateException.class,
                () -> service.createReservation("user-2", event.getId(), "STANDARD", 10));

        service.expireReservation(expiring.getId());
        Reservation paid = service.createReservation("user-2", event.getId(), "EARLY_BIRD", 10);
        service.confirmPayment(paid.getId());

        assertEquals(ReservationStatus.EXPIRED, expiring.getState());
        assertEquals(ReservationStatus.CONFIRMED, paid.getState());
        verify(publisher).publish(argThat(message ->
                TicketingMessaging.RESERVATION_CREATED.equals(message.eventType())
                        && message.reservationId().equals(expiring.getId())));
        verify(publisher).publish(argThat(message ->
                TicketingMessaging.RESERVATION_EXPIRED.equals(message.eventType())
                        && message.reservationId().equals(expiring.getId())));
        verify(publisher).publish(argThat(message ->
                TicketingMessaging.RESERVATION_CREATED.equals(message.eventType())
                        && message.reservationId().equals(paid.getId())));
        verify(publisher).publish(argThat(message ->
                TicketingMessaging.PAYMENT_COMPLETED.equals(message.eventType())
                        && message.reservationId().equals(paid.getId())));
    }
}
