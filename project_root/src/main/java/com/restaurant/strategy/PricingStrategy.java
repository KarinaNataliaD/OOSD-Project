package com.restaurant.strategy;

public interface PricingStrategy {

    double calculateTotal(double subtotal);

    String getLabel();
}