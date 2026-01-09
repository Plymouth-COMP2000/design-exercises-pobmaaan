package com.example.restaurantapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.restaurantapp.model.AppDatabase;
import com.example.restaurantapp.model.MenuItem;
import com.example.restaurantapp.model.MenuItemDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuViewModel extends AndroidViewModel {

    private final MenuItemDao menuItemDao;
    private final LiveData<List<MenuItem>> allMenuItems;
    private final ExecutorService executorService;

    public MenuViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        menuItemDao = db.menuItemDao();
        // Room can now return LiveData directly, so we don't need to manage it with MutableLiveData and threads manually
        allMenuItems = menuItemDao.getAllMenuItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<MenuItem>> getAllMenuItems() {
        return allMenuItems;
    }

    public void insert(MenuItem menuItem) {
        executorService.execute(() -> menuItemDao.insert(menuItem));
    }

    public void update(MenuItem menuItem) {
        executorService.execute(() -> menuItemDao.update(menuItem));
    }

    public void delete(MenuItem menuItem) {
        executorService.execute(() -> menuItemDao.delete(menuItem));
    }
}
