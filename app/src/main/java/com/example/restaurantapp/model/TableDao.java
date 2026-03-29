package com.example.restaurantapp.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TableDao {
    @Insert
    void insert(Table table);

    @Query("SELECT * FROM tables")
    LiveData<List<Table>> getAllTables();

    @Query("SELECT * FROM tables WHERE seats >= :partySize ORDER BY seats ASC LIMIT 1")
    LiveData<Table> findTableForSize(int partySize);

    @Query("DELETE FROM tables")
    void deleteAll();
}
