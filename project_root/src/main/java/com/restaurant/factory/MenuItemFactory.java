package com.restaurant.factory;

import com.restaurant.domain.MenuItem;

public class MenuItemFactory {

    private MenuItemFactory() {

    } 


    public static MenuItem create(String type) {
        return switch (type.toLowerCase().trim()) {

            // ── Burgers ──────────────────────────────────────────────────────
            case "burger"         -> new Burger("Classic Burger",         8.99);
            case "burger-deluxe"  -> new Burger("Deluxe Burger",         11.99);

            // ── Pizzas ───────────────────────────────────────────────────────
            case "pizza"          -> new Pizza("Margherita Pizza",       10.99);
            case "pizza-pepperoni"-> new Pizza("Pepperoni Pizza",        12.99);
            case "pizza-veggie"   -> new Pizza("Veggie Pizza",           11.49);

            // ── Drinks ───────────────────────────────────────────────────────
            case "drink"          -> new Drink("Soft Drink",              2.49);
            case "drink-soda"     -> new Drink("Soda",                    2.49);
            case "drink-juice"    -> new Drink("Fresh Juice",             3.49);
            case "drink-water"    -> new Drink("Still Water",             1.49);

            default -> throw new IllegalArgumentException(
                "Unknown menu item type: '" + type + "'. " +
                "Valid types: burger, burger-deluxe, pizza, pizza-pepperoni, " +
                "pizza-veggie, drink, drink-soda, drink-juice, drink-water"
            );
        };
    }
}