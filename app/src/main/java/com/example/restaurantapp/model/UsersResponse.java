package com.example.restaurantapp.model;

import java.util.List;

public class UsersResponse {
    private final List<User> users;

    public UsersResponse(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
