# Restaurant Management Application

This is an Android application developed for the COMP2000 Software Engineering 2 module. It serves as a comprehensive restaurant management system, providing distinct functionalities for both restaurant staff and guests.

## Overview

The application is divided into two primary user experiences:

-   **Staff Side:** Allows restaurant employees to manage menu items and handle customer reservations efficiently.
-   **Guest Side:** Enables customers to browse the restaurant's menu and make table reservations directly from their mobile devices.

The app utilizes a local SQLite database for fast, offline-first access to menu and reservation data, while interacting with a secure RESTful API for user authentication.

## Features

### Staff Features

-   **Secure Login:** Staff must log in to access management features.
-   **Menu Management:** Add, edit, and delete menu items, including details like name, price, and dietary information.
-   **Reservation Management:** View all customer reservations, create new bookings, and cancel existing ones.
-   **User Management:** View and manage user accounts within the system.
-   **Notifications:** Receive alerts for new or modified reservations.

### Guest Features

-   **Secure Login/Sign-up:** Guests can create an account and log in.
-   **Menu Browsing:** Browse the full restaurant menu with detailed descriptions and prices.
-   **Table Reservations:** Make a new table reservation for a specific date, time, and party size.
-   **Manage Bookings:** View, edit, or cancel their own upcoming reservations.
-   **Notifications:** Receive confirmations and updates about their reservation status.

## Technical Details & Architecture

-   **Language:** Java
-   **Platform:** Android
-   **Architecture:** The application is built using the **Model-View-ViewModel (MVVM)** architecture to separate UI logic from business logic, promoting a clean and maintainable codebase.
-   **Key Patterns:**
    -   **Repository Pattern:** Abstracts the data sources (local database and remote API) from the rest of the app.
    -   **Singleton Pattern:** Used for database and network client instantiation to ensure a single, shared instance.
-   **Data Management:**
    -   **Local Database:** The **Room Persistence Library** is used for creating and managing a local SQLite database for menu and reservation data.
    -   **Remote API:** **Retrofit** is used for handling all network communication with the RESTful API for user authentication.
-   **Navigation:** The app's navigation is handled by the **Android Jetpack Navigation Component**, enabling a consistent and predictable user flow.

## Key Dependencies

-   `androidx.room:room-runtime`: For local database storage.
-   `com.squareup.retrofit2:retrofit` & `com.squareup.retrofit2:converter-gson`: For network API calls and JSON parsing.
-   `androidx.navigation:navigation-fragment` & `androidx.navigation:navigation-ui`: For in-app navigation.
-   `com.google.android.material:material`: For modern UI components following Material Design guidelines.
-   `androidx.databinding:viewbinding`: For type-safe view access in layouts.
