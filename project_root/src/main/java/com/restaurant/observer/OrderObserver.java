package com.restaurant.observer;

import com.restaurant.domain.Order;

public interface OrderObserver {
    /**
     * Called by OrderQueue whenever an order's state changes.
     *
     * @param order  the order that changed
     * @param event  human-readable status string (e.g. "PLACED", "READY")
     */
    void onOrderUpdate(Order order, String event);
}