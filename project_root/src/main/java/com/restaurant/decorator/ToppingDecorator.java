package com.restaurant.decorator;

import com.restaurant.domain.MenuItem;

public abstract class ToppingDecorator implements MenuItem {

    protected final MenuItem wrapped;

    protected ToppingDecorator(MenuItem wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    // Subclasses must override getDescription() and getPrice()
}