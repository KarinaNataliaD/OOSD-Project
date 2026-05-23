package com.restaurant.decorator;

import com.restaurant.domain.MenuItem;

/** Adds extra sauce to any MenuItem (+$0.50). */
public class SauceDecorator extends ToppingDecorator {

    private static final double PRICE = 0.50;
    private static final String LABEL = "Extra Sauce";

    public SauceDecorator(MenuItem wrapped) {
        super(wrapped);
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + " + LABEL;
    }

    @Override
    public double getPrice() {
        return wrapped.getPrice() + PRICE;
    }
}