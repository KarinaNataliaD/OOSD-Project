package com.restaurant.domain;

/**
 * Represents a restaurant customer.
 * loyaltyPoints determine whether LoyaltyPricing strategy applies.
 */
public class Customer {

    private final String name;
    private int loyaltyPoints;

    public Customer(String name, int loyaltyPoints) {
        this.name          = name;
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getName()        { return name; }
    public int getLoyaltyPoints()  { return loyaltyPoints; }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }

    public boolean isLoyaltyMember() {
        return loyaltyPoints >= 100;
    }

    @Override
    public String toString() {
        return String.format("Customer{name='%s', loyaltyPoints=%d}", name, loyaltyPoints);
    }
}