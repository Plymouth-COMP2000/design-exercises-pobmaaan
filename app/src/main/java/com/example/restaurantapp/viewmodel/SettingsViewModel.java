package com.example.restaurantapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SettingsViewModel extends AndroidViewModel {

    private final SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "notification_prefs";

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public LiveData<Boolean> getNotificationEnabled(String key) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        liveData.setValue(sharedPreferences.getBoolean(key, false));
        return liveData;
    }

    public void setNotificationEnabled(String key, boolean isEnabled) {
        sharedPreferences.edit().putBoolean(key, isEnabled).apply();
    }
}
