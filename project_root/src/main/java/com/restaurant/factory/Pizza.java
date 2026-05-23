package com.restaurant.factory;

import com.restaurant.domain.MenuItem;

public class Pizza implements MenuItem {

    private final String name;
    private final double basePrice;

    public Pizza(String name, double basePrice) {
        this.name      = name;
        this.basePrice = basePrice;
    }

    @Override public String getName()        { return name; }
    @Override public String getDescription() { return name; }
    @Override public double getPrice()       { return basePrice; }
}