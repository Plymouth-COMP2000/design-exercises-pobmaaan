package com.example.restaurantapp.model;

public class UserResponse {
    private final User user;

    public UserResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
