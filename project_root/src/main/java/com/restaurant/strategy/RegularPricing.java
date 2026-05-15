package com.restaurant.strategy;

public class RegularPricing implements PricingStrategy {

    @Override
    public double calculateTotal(double subtotal) {
        return subtotal; // No discount for regular pricing
    }

    @Override
    public String getLabel() {
        return "Regular Pricing (No Discount)";
    }
}