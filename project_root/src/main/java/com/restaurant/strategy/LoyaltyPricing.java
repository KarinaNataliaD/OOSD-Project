package com.restaurant.strategy;
 
public class LoyaltyPricing implements PricingStrategy {
 
    private static final double DISCOUNT_RATE = 0.15;
 
    @Override
    public double calculateTotal(double subtotal) {
        return subtotal * (1.0 - DISCOUNT_RATE);
    }
 
    @Override
    public String getLabel() {
        return String.format("Loyalty Member Pricing (%.0f%% off)", DISCOUNT_RATE * 100);
    }
}
 