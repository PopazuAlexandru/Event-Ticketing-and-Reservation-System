package com.example.ticketing.domain.pricing;

/**
 * Applies no discount to the base ticket price.
 */
public class StandardPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return validate(basePrice);
    }

    static double validate(double basePrice) {
        if (!Double.isFinite(basePrice) || basePrice < 0) {
            throw new IllegalArgumentException("basePrice must be a finite, non-negative value");
        }
        return basePrice;
    }
}
