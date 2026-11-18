package com.example.ticketing.domain.pricing;

/**
 * Calculates the customer-facing price for a ticket.
 */
public interface PricingStrategy {
    double calculatePrice(double basePrice);
}
