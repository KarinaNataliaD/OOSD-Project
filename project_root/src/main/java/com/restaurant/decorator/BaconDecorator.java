package com.restaurant.decorator;

import com.restaurant.domain.MenuItem;

/** Adds bacon to any MenuItem (+$1.25). */
public class BaconDecorator extends ToppingDecorator {

    private static final double PRICE = 1.25;
    private static final String LABEL = "Bacon";

    public BaconDecorator(MenuItem wrapped) {
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