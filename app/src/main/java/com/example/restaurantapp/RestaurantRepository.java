package com.example.restaurantapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.restaurantapp.model.AppDatabase;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.model.ReservationDao;
import com.example.restaurantapp.model.Table;
import com.example.restaurantapp.model.TableDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestaurantRepository {
    private static RestaurantRepository instance;

    private final TableDao tableDao;
    private final ReservationDao reservationDao;
    private final LiveData<List<Table>> allTables;
    private final LiveData<List<Reservation>> allReservations;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static RestaurantRepository getInstance(Application application) {
        if (instance == null) {
            instance = new RestaurantRepository(application);
        }
        return instance;
    }

    private RestaurantRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        tableDao = db.tableDao();
        reservationDao = db.reservationDao();
        allTables = tableDao.getAllTables();
        allReservations = reservationDao.getAllReservations();
    }

    public LiveData<List<Table>> getTables() {
        return allTables;
    }

    public LiveData<List<Reservation>> getReservations() {
        return allReservations;
    }

    public LiveData<Table> findTableForSize(int partySize) {
        return tableDao.findTableForSize(partySize);
    }

    public void addReservation(Reservation reservation) {
        executorService.execute(() -> reservationDao.insert(reservation));
    }

    public void deleteReservation(Reservation reservation) {
        executorService.execute(() -> reservationDao.delete(reservation));
    }
}
