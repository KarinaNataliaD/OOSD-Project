package com.restaurant.command;

import com.restaurant.domain.Order;
import com.restaurant.domain.OrderQueue;
import com.restaurant.domain.OrderStatus;


public class AdvanceOrderCommand implements Command {
 
    private final Order      order;
    private final OrderQueue queue;
    private OrderStatus      previousStatus;
 
    public AdvanceOrderCommand(Order order, OrderQueue queue) {
        this.order = order;
        this.queue = queue;
    }
 
    @Override
    public void execute() {
        previousStatus = order.getStatus();
        queue.advanceOrder(order);
    }
 
    @Override
    public void undo() {
        System.out.printf("  [UNDO] Reverting Order #%d from %s back to %s%n",
                order.getId(), order.getStatus(), previousStatus);
        order.setStatus(previousStatus);
    }
 
    @Override
    public String getDescription() {
        return String.format("Advance Order #%d (was: %s)", order.getId(), previousStatus);
    }
}