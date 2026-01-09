package com.example.restaurantapp.model;

public class MenuCategory implements MenuItemSealed {
    private final String name;

    public MenuCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
