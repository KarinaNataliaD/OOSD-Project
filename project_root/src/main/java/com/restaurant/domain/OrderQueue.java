package com.restaurant.domain;

import com.restaurant.observer.OrderObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OrderQueue {

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static OrderQueue instance;

    private OrderQueue() {}   // private constructor prevents external instantiation

    public static synchronized OrderQueue getInstance() {
        if (instance == null) {
            instance = new OrderQueue();
        }
        return instance;
    }

    // ── State ─────────────────────────────────────────────────────────────────

    private final List<Order>         orders    = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    // ── Observer management ───────────────────────────────────────────────────

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Order order, String event) {
        for (OrderObserver observer : observers) {
            observer.onOrderUpdate(order, event);
        }
    }

    // ── Queue operations ──────────────────────────────────────────────────────

    /** Adds a new order and notifies all observers. */
    public void placeOrder(Order order) {
        orders.add(order);
        order.setStatus(OrderStatus.PLACED);
        notifyObservers(order, "PLACED");
    }

    /** Advances the order status by one step and notifies observers. */
    public void advanceOrder(Order order) {
        switch (order.getStatus()) {
            case PLACED    -> { order.setStatus(OrderStatus.PREPARING);  notifyObservers(order, "PREPARING"); }
            case PREPARING -> { order.setStatus(OrderStatus.READY);      notifyObservers(order, "READY"); }
            case READY     -> { order.setStatus(OrderStatus.SERVED);   notifyObservers(order, "SERVED"); }
            default        -> System.out.println("Order #" + order.getId() + " cannot be advanced further.");
        }
    }

    /** Cancels an order and notifies observers. */
    public void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.SERVED) {
            System.out.println("Cannot cancel a served order.");
            return;
        }
        order.setStatus(OrderStatus.CANCELLED);
        orders.remove(order);
        notifyObservers(order, "CANCELLED");
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public int size() { return orders.size(); }
}