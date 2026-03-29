package com.example.restaurantapp.model;

public class MenuItemDetails implements MenuItemSealed {
    private final int id;
    private final String name;
    private final String allergens;
    private final String details;
    private final String price;

    public MenuItemDetails(int id, String name, String allergens, String details, String price) {
        this.id = id;
        this.name = name;
        this.allergens = allergens;
        this.details = details;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAllergens() {
        return allergens;
    }

    public String getDetails() {
        return details;
    }

    public String getPrice() {
        return price;
    }
}
