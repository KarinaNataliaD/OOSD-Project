package com.restaurant.observer;

import com.restaurant.domain.Order;
/**
 * Concrete Observer — represents the kitchen display screen.
 * Reacts to PLACED and PREPARING events by printing a kitchen ticket.
 */
public class KitchenObserver implements OrderObserver {

    @Override
    public void onOrderUpdate(Order order, String event) {
        switch (event) {
            case "PLACED" -> {
                System.out.println();
                System.out.println("  [KITCHEN] New order received!");
                System.out.println("  --------------------------------");
                System.out.printf ("  Order #%d for %s%n", order.getId(), order.getCustomerName());
                order.getItems().forEach(item ->
                    System.out.printf("    * %s%n", item.getDescription()));
                System.out.println("  --------------------------------");
                System.out.println("  Status: Start preparing now.");
            }
            case "PREPARING" -> {
                System.out.printf("  [KITCHEN] Order #%d is now being prepared.%n", order.getId());
            }
            case "CANCELLED" -> {
                System.out.printf("  [KITCHEN] Order #%d CANCELLED — discard if started.%n", order.getId());
            }
            default -> { /* kitchen doesn't care about READY/DELIVERED */ }
        }
    }
}