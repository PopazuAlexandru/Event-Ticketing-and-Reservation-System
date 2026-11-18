package com.example.ticketing.domain.pricing;

/**
 * Applies a 20 percent early-bird discount.
 */
public class EarlyBirdPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return StandardPricing.validate(basePrice) * 0.80;
    }
}
