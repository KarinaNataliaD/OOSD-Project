package com.restaurant.domain;

import com.restaurant.strategy.PricingStrategy;
import com.restaurant.strategy.RegularPricing;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a customer order.
 *
 * Participates in:
 *  - Command pattern  : target object that PlaceOrderCommand / CancelOrderCommand act on
 *  - Strategy pattern : holds a PricingStrategy to calculate the final total
 *  - Observer pattern : OrderQueue notifies observers when this order's status changes
 */
public class Order {

    private static int nextId = 1;

    private final int id;
    private final String customerName;
    private final List<MenuItem> items;
    private OrderStatus status;
    private PricingStrategy pricingStrategy;
    private final LocalTime createdAt;

    public Order(String customerName) {
        this.id            = nextId++;
        this.customerName  = customerName;
        this.items         = new ArrayList<>();
        this.status        = OrderStatus.PLACED;
        this.pricingStrategy = new RegularPricing();   // default strategy
        this.createdAt     = LocalTime.now();
    }

    // ── item management ──────────────────────────────────────────────────────

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // ── pricing (Strategy pattern) ────────────────────────────────────────────

    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }

    /** Returns the final total after applying the active pricing strategy. */
    public double getTotal() {
        double subtotal = items.stream()
                               .mapToDouble(MenuItem::getPrice)
                               .sum();
        return pricingStrategy.calculateTotal(subtotal);
    }

    public String getPricingLabel() {
        return pricingStrategy.getLabel();
    }

    // ── status ────────────────────────────────────────────────────────────────

    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) { this.status = status; }

    // ── accessors ─────────────────────────────────────────────────────────────

    public int getId()               { return id; }
    public String getCustomerName()  { return customerName; }
    public LocalTime getCreatedAt()  { return createdAt; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order #%d  [%s]  Customer: %s%n", id, status, customerName));
        sb.append(String.format("  Created : %s%n", createdAt));
        sb.append(String.format("  Pricing : %s%n", getPricingLabel()));
        items.forEach(i -> sb.append(String.format("    - %-25s $%.2f%n",
                                                    i.getDescription(), i.getPrice())));
        sb.append(String.format("  TOTAL   : $%.2f%n", getTotal()));
        return sb.toString();
    }
}