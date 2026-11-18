package com.example.ticketing.service;

import com.example.ticketing.domain.event.Event;
import com.example.ticketing.domain.event.EventBuilder;
import com.example.ticketing.domain.pricing.EarlyBirdPricing;
import com.example.ticketing.domain.pricing.GroupDiscountPricing;
import com.example.ticketing.domain.pricing.PricingStrategy;
import com.example.ticketing.domain.pricing.StandardPricing;
import com.example.ticketing.domain.pricing.TicketCategory;
import com.example.ticketing.domain.reservation.Reservation;
import com.example.ticketing.domain.reservation.ReservationStatus;
import com.example.ticketing.messaging.TicketingEvent;
import com.example.ticketing.messaging.TicketingEventPublisher;
import com.example.ticketing.messaging.TicketingMessaging;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Coordinates event capacity, reservation lifecycle, pricing, and notifications.
 */
@Service
public class ReservationService {
    private final TicketingEventPublisher eventPublisher;
    private final Map<UUID, Event> events = new ConcurrentHashMap<>();
    private final Map<UUID, Reservation> reservations = new ConcurrentHashMap<>();

    public ReservationService(TicketingEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void registerEvent(Event event) {
        events.put(event.getId(), event);
    }

    public Event createEvent(String title, String location, LocalDateTime dateTime, int totalCapacity) {
        Event event = new EventBuilder()
                .title(title)
                .location(location)
                .dateTime(dateTime)
                .totalCapacity(totalCapacity)
                .build();
        registerEvent(event);
        return event;
    }

    public List<Event> getEvents() {
        return List.copyOf(events.values());
    }

    public Event getEvent(UUID eventId) {
        return findEvent(eventId);
    }

    public List<String> getTicketCategories() {
        return List.of("STANDARD", "EARLY_BIRD", "GROUP_DISCOUNT");
    }

    public synchronized Reservation createReservation(
            String userId, UUID eventId, String pricingType, double basePrice) {
        Event event = findEvent(eventId);
        long activeReservations = reservations.values().stream()
                .filter(reservation -> reservation.getEventId().equals(eventId))
                .filter(reservation -> reservation.getState() == ReservationStatus.PENDING
                        || reservation.getState() == ReservationStatus.CONFIRMED)
                .count();
        if (activeReservations >= event.getTotalCapacity()) {
            throw new IllegalStateException("Event has reached its reservation capacity");
        }

        PricingStrategy pricingStrategy = strategyFor(pricingType);
        TicketCategory category = new TicketCategory(
                pricingType.trim().toUpperCase(Locale.ROOT), basePrice, pricingStrategy);
        Reservation reservation = new Reservation(userId, eventId, category.getName(), category.getPrice());
        reservations.put(reservation.getId(), reservation);
        publish(TicketingMessaging.RESERVATION_CREATED, reservation, "Reservation created");
        return reservation;
    }

    public Reservation confirmPayment(UUID reservationId) {
        Reservation reservation = findReservation(reservationId);
        reservation.confirmPayment();
        publish(TicketingMessaging.PAYMENT_COMPLETED, reservation, "Payment succeeded");
        return reservation;
    }

    public Reservation expireReservation(UUID reservationId) {
        Reservation reservation = findReservation(reservationId);
        reservation.expire();
        publish(TicketingMessaging.RESERVATION_EXPIRED, reservation, "Reservation expired");
        return reservation;
    }

    public Reservation cancelReservation(UUID reservationId) {
        Reservation reservation = findReservation(reservationId);
        reservation.cancel();
        return reservation;
    }

    public Reservation getReservation(UUID reservationId) {
        return findReservation(reservationId);
    }

    private Event findEvent(UUID eventId) {
        Event event = events.get(eventId);
        if (event == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        return event;
    }

    private Reservation findReservation(UUID reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
        return reservation;
    }

    private void publish(String eventType, Reservation reservation, String message) {
        eventPublisher.publish(new TicketingEvent(
                eventType,
                reservation.getEventId(),
                reservation.getId(),
                reservation.getUserId(),
                Instant.now(),
                message));
    }

    private PricingStrategy strategyFor(String pricingType) {
        if (pricingType == null || pricingType.isBlank()) {
            throw new IllegalArgumentException("pricingType must not be blank");
        }
        return switch (pricingType.trim().toUpperCase(Locale.ROOT)) {
            case "STANDARD" -> new StandardPricing();
            case "EARLY_BIRD" -> new EarlyBirdPricing();
            case "GROUP_DISCOUNT" -> new GroupDiscountPricing();
            default -> throw new IllegalArgumentException("Unknown pricing type: " + pricingType);
        };
    }
}
