package com.restaurant.domain;

/**
 * Core interface for all menu items.
 * Used by Factory Method (products) and Decorator (component + wrappers).
 */
public interface MenuItem {
    String getName();
    String getDescription();
    double getPrice();
}