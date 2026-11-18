package com.example.ticketing.web;

import java.util.UUID;

/**
 * Request payload for creating a reservation.
 */
public record CreateReservationRequest(
        String userId,
        UUID eventId,
        String pricingType,
        double basePrice) {
}
