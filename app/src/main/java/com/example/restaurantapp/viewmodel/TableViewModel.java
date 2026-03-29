package com.example.restaurantapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.restaurantapp.RestaurantRepository;
import com.example.restaurantapp.model.Table;

import java.util.List;

public class TableViewModel extends AndroidViewModel {

    private final RestaurantRepository repository;
    private final LiveData<List<Table>> allTables;

    public TableViewModel(Application application) {
        super(application);
        repository = RestaurantRepository.getInstance(application);
        allTables = repository.getTables();
    }

    public LiveData<List<Table>> getAllTables() {
        return allTables;
    }

    public LiveData<Table> findTableForSize(int partySize) {
        return repository.findTableForSize(partySize);
    }
}
