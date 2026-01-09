package com.example.restaurantapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tables")
public class Table {
    @PrimaryKey
    private final int id;
    private final int seats;

    public Table(int id, int seats) {
        this.id = id;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public int getSeats() {
        return seats;
    }
}
