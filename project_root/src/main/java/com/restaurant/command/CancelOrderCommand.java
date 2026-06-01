package com.restaurant.command;

import com.restaurant.domain.Order;
import com.restaurant.domain.OrderQueue;
import com.restaurant.domain.OrderStatus;

/**
 * Concrete Command — cancels an active order.
 * Undo re-places the order back into the queue.
 */
public class CancelOrderCommand implements Command {

    private final Order       order;
    private final OrderQueue  queue;
    private OrderStatus       previousStatus;

    public CancelOrderCommand(Order order, OrderQueue queue) {
        this.order = order;
        this.queue = queue;
    }

    @Override
    public void execute() {
        previousStatus = order.getStatus();   // snapshot for undo
        queue.cancelOrder(order);
    }

    @Override
    public void undo() {
        System.out.printf("  [UNDO] Restoring Order #%d to %s%n",
                order.getId(), previousStatus);
        order.setStatus(previousStatus);
        queue.placeOrder(order);
    }

    @Override
    public String getDescription() {
        return String.format("Cancel Order #%d for %s", order.getId(), order.getCustomerName());
    }
}