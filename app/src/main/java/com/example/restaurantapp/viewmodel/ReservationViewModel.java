package com.example.restaurantapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.restaurantapp.model.AppDatabase;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.model.ReservationDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationViewModel extends AndroidViewModel {

    private final ReservationDao reservationDao;
    private final LiveData<List<Reservation>> allReservations;
    private final ExecutorService executorService;

    public ReservationViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        reservationDao = db.reservationDao();
        allReservations = reservationDao.getAllReservations();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Reservation>> getAllReservations() {
        return allReservations;
    }

    public void insert(Reservation reservation) {
        executorService.execute(() -> reservationDao.insert(reservation));
    }

    public void update(Reservation reservation) {
        executorService.execute(() -> reservationDao.update(reservation));
    }

    public void delete(Reservation reservation) {
        executorService.execute(() -> reservationDao.delete(reservation));
    }
}
