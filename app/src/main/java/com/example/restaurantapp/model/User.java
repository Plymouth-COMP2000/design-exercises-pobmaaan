package com.example.restaurantapp.model;

public class User {
    private final String username;
    private final String password;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String contact;
    private final String usertype;

    public User(String username, String password, String firstname, String lastname, String email, String contact, String usertype) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.usertype = usertype;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getUsertype() {
        return usertype;
    }
}
