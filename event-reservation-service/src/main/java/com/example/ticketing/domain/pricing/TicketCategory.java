package com.example.ticketing.domain.pricing;

import java.util.Objects;

/**
 * Ticket type with a base price and a swappable pricing strategy.
 */
public class TicketCategory {
    private final String name;
    private final double basePrice;
    private PricingStrategy pricingStrategy;

    public TicketCategory(String name, double basePrice, PricingStrategy pricingStrategy) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        StandardPricing.validate(basePrice);
        this.name = name;
        this.basePrice = basePrice;
        this.pricingStrategy = Objects.requireNonNull(pricingStrategy, "pricingStrategy must not be null");
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = Objects.requireNonNull(pricingStrategy, "pricingStrategy must not be null");
    }

    public double getPrice() {
        return pricingStrategy.calculatePrice(basePrice);
    }
}
