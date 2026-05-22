package com.restaurant.observer;

import com.restaurant.domain.Order;

public class CustomerObserver implements OrderObserver {

    @Override
    public void onOrderUpdate(Order order, String event) {
        switch (event) {
            case "PLACED" -> {
                System.out.printf("  [CUSTOMER] Hi %s! Order #%d received. We'll get started shortly.%n",
                        order.getCustomerName(), order.getId());
            }
            case "READY" -> {
                System.out.printf("  [CUSTOMER] Order #%d is ready for collection, %s!%n",
                        order.getId(), order.getCustomerName());
            }
            case "DELIVERED" -> {
                System.out.printf("  [CUSTOMER] Order #%d delivered. Enjoy your meal, %s! Total: $%.2f%n",
                        order.getId(), order.getCustomerName(), order.getTotal());
            }
            case "CANCELLED" -> {
                System.out.printf("  [CUSTOMER] Sorry %s, Order #%d has been cancelled.%n",
                        order.getCustomerName(), order.getId());
            }
            default -> { /* ignore PREPARING — no customer message needed */ }
        }
    }
}