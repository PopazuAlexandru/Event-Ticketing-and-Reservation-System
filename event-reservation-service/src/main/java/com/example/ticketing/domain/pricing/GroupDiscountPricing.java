package com.example.ticketing.domain.pricing;

/**
 * Applies the 15 percent bulk-order discount.
 */
public class GroupDiscountPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return StandardPricing.validate(basePrice) * 0.85;
    }
}
