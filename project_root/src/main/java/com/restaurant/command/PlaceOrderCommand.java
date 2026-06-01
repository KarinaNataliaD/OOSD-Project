package com.restaurant.command;

import com.restaurant.domain.Order;
import com.restaurant.domain.OrderQueue;

/**
 * Concrete Command — places an order into the queue.
 * Undo removes it from the queue and cancels it.
 */
public class PlaceOrderCommand implements Command {

    private final Order      order;
    private final OrderQueue queue;

    public PlaceOrderCommand(Order order, OrderQueue queue) {
        this.order = order;
        this.queue = queue;
    }

    @Override
    public void execute() {
        queue.placeOrder(order);
    }

    @Override
    public void undo() {
        System.out.printf("  [UNDO] Reversing place-order for Order #%d%n", order.getId());
        queue.cancelOrder(order);
    }

    @Override
    public String getDescription() {
        return String.format("Place Order #%d for %s", order.getId(), order.getCustomerName());
    }
}