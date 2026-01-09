package com.example.restaurantapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String reservationName = intent.getStringExtra("reservation_name");
        String reservationTime = intent.getStringExtra("reservation_time");

        NotificationManager.sendNotification(
                context,
                "reservation_reminder",
                "Reservation Reminder",
                "You have a reservation for " + reservationName + " at " + reservationTime
        );
    }
}
