package com.example.restaurantapp.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MenuItemDao {
    @Insert
    void insert(MenuItem menuItem);

    @Update
    void update(MenuItem menuItem);

    @Delete
    void delete(MenuItem menuItem);

    @Query("SELECT * FROM menu_items")
    LiveData<List<MenuItem>> getAllMenuItems();

    @Query("SELECT * FROM menu_items WHERE category = :category")
    LiveData<List<MenuItem>> getMenuItemsByCategory(String category);
}
