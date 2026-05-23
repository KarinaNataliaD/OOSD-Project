package com.restaurant.decorator;

import com.restaurant.domain.MenuItem;

/** Adds extra cheese to any MenuItem (+$0.75). */
public class CheeseDecorator extends ToppingDecorator {

    private static final double PRICE  = 0.75;
    private static final String LABEL  = "Extra Cheese";

    public CheeseDecorator(MenuItem wrapped) {
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