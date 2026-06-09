package com.restaurant;

import com.restaurant.command.*;
import com.restaurant.decorator.*;
import com.restaurant.domain.*;
import com.restaurant.factory.MenuItemFactory;
import com.restaurant.observer.*;
import com.restaurant.strategy.*;

public class Main {

    public static void main(String[] args) {

        separator("RESTAURANT ORDERING SYSTEM — DESIGN PATTERN DEMO");

        // ── 1. SINGLETON ──────────────────────────────────────────────────────
        section("1 · SINGLETON — OrderQueue");

        OrderQueue queue  = OrderQueue.getInstance();
        OrderQueue queue2 = OrderQueue.getInstance();
        System.out.println("  queue == queue2 : " + (queue == queue2));   // true — same instance
        System.out.println("  Both variables point to the single OrderQueue instance.");

        // ── 4. OBSERVER — register before placing orders ───────────────────────
        section("4 · OBSERVER — Registering Kitchen + Customer screens");

        KitchenObserver  kitchenScreen  = new KitchenObserver();
        CustomerObserver customerScreen = new CustomerObserver();

        queue.addObserver(kitchenScreen);
        queue.addObserver(customerScreen);
        System.out.println("  KitchenObserver  registered.");
        System.out.println("  CustomerObserver registered.");

        // ── 2. FACTORY METHOD ─────────────────────────────────────────────────
        section("2 · FACTORY METHOD — Building menu items");

        MenuItem burger      = MenuItemFactory.create("burger");
        MenuItem pizza       = MenuItemFactory.create("pizza-pepperoni");
        MenuItem drink       = MenuItemFactory.create("drink-juice");
        MenuItem deluxeBurger= MenuItemFactory.create("burger-deluxe");

        System.out.printf("  Created: %-25s $%.2f%n", burger.getDescription(),       burger.getPrice());
        System.out.printf("  Created: %-25s $%.2f%n", pizza.getDescription(),        pizza.getPrice());
        System.out.printf("  Created: %-25s $%.2f%n", drink.getDescription(),        drink.getPrice());
        System.out.printf("  Created: %-25s $%.2f%n", deluxeBurger.getDescription(), deluxeBurger.getPrice());

        // ── 3. DECORATOR ──────────────────────────────────────────────────────
        section("3 · DECORATOR — Customising items with toppings");

        // Wrap burger: base → cheese → bacon
        MenuItem customBurger = new BaconDecorator(
                                new CheeseDecorator(burger));

        // Wrap pizza: base → extra sauce → cheese
        MenuItem customPizza  = new CheeseDecorator(
                                new SauceDecorator(pizza));

        System.out.printf("  %-40s $%.2f%n", customBurger.getDescription(), customBurger.getPrice());
        System.out.printf("  %-40s $%.2f%n", customPizza.getDescription(),  customPizza.getPrice());
        System.out.println("  (Each decorator adds its price to the wrapped item's price)");

        // ── 6. STRATEGY — create customers + set pricing ─────────────────────
        section("6 · STRATEGY — Pricing strategies");

        Customer alice = new Customer("Alice",   0);    // regular customer
        Customer bob   = new Customer("Bob",   150);    // loyalty member
        Customer carol = new Customer("Carol",  50);    // regular, but happy-hour demo

        // Build three orders using the same items
        Order order1 = new Order(alice.getName());
        order1.addItem(customBurger);
        order1.addItem(drink);
        // default RegularPricing applied automatically

        Order order2 = new Order(bob.getName());
        order2.addItem(customBurger);
        order2.addItem(drink);
        if (bob.isLoyaltyMember()) {
            order2.setPricingStrategy(new LoyaltyPricing());
        }

        Order order3 = new Order(carol.getName());
        order3.addItem(customPizza);
        order3.addItem(drink);
        order3.setPricingStrategy(new HappyHourPricing());   // 3pm–5pm

        System.out.printf("  %-20s strategy: %-40s total: $%.2f%n",
                alice.getName(), order1.getPricingLabel(), order1.getTotal());
        System.out.printf("  %-20s strategy: %-40s total: $%.2f%n",
                bob.getName(),   order2.getPricingLabel(), order2.getTotal());
        System.out.printf("  %-20s strategy: %-40s total: $%.2f%n",
                carol.getName(), order3.getPricingLabel(), order3.getTotal());

        // ── 5. COMMAND — place, advance, cancel, undo ────────────────────────
        section("5 · COMMAND — Placing orders through CommandHistory");

        CommandHistory history = new CommandHistory();

        // Place all three orders via commands (observers fire on each execute)
        history.execute(new PlaceOrderCommand(order1, queue));
        history.execute(new PlaceOrderCommand(order2, queue));
        history.execute(new PlaceOrderCommand(order3, queue));

        System.out.println("\n  Queue size after 3 placements: " + queue.size());

        // Advance order1 twice: PLACED → PREPARING → READY
        section("  Advancing order1 through the lifecycle");
        history.execute(new AdvanceOrderCommand(order1, queue));
        history.execute(new AdvanceOrderCommand(order1, queue));

        // Undo the last advance (READY → PREPARING)
        section("  Undoing last advance on order1");
        history.undo();
        System.out.println("  order1 status after undo: " + order1.getStatus());

        // Cancel order3 via command
        section("  Cancelling order3 via CancelOrderCommand");
        history.execute(new CancelOrderCommand(order3, queue));
        System.out.println("  Queue size after cancel: " + queue.size());

        // Undo the cancel — order3 is restored
        section("  Undoing the cancel — order3 restored");
        history.undo();
        System.out.println("  Queue size after undo cancel: " + queue.size());
        System.out.println("  order3 status: " + order3.getStatus());

        // ── Final receipt printout ────────────────────────────────────────────
        section("FINAL ORDER RECEIPTS");
        queue.getOrders().forEach(o -> {
            System.out.println();
            System.out.print(o);
        });

        separator("END OF DEMO");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static void separator(String title) {
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("  ── " + title + " ──");
    }
}