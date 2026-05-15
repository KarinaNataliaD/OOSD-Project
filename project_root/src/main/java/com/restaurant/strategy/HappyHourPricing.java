package com.restaurant.strategy; 

public class HappyHourPricing implements PricingStrategy {

    private static final double DISCOUNT_RATE = 0.20; // 20% discount

    @Override
    public double calculateTotal(double subtotal) {
        return subtotal * (1.0 - DISCOUNT_RATE); // Apply discount
    }

    @Override
    public String getLabel() {
        return String.format("Happy Hour Pricing (%.0f%% Discount)", DISCOUNT_RATE * 100);
    }
}