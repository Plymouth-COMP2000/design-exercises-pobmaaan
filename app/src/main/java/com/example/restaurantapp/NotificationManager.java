package com.example.restaurantapp;

import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationManager {

    private static final String MENU_CHANGES_CHANNEL_ID = "menu_changes";
    private static final String NEW_RESERVATION_CHANNEL_ID = "new_reservation";
    private static final String RESERVATION_CHANGES_CHANNEL_ID = "reservation_changes";
    private static final String RESERVATION_CONFIRMATION_CHANNEL_ID = "reservation_confirmation";
    private static final String RESERVATION_REMINDER_CHANNEL_ID = "reservation_reminder";

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel menuChangesChannel = new NotificationChannel(
                    MENU_CHANGES_CHANNEL_ID,
                    "Menu Changes",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationChannel newReservationChannel = new NotificationChannel(
                    NEW_RESERVATION_CHANNEL_ID,
                    "New Reservation",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationChannel reservationChangesChannel = new NotificationChannel(
                    RESERVATION_CHANGES_CHANNEL_ID,
                    "Reservation Changes",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationChannel reservationConfirmationChannel = new NotificationChannel(
                    RESERVATION_CONFIRMATION_CHANNEL_ID,
                    "Reservation Confirmation",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationChannel reservationReminderChannel = new NotificationChannel(
                    RESERVATION_REMINDER_CHANNEL_ID,
                    "Reservation Reminder",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );

            android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(menuChangesChannel);
            notificationManager.createNotificationChannel(newReservationChannel);
            notificationManager.createNotificationChannel(reservationChangesChannel);
            notificationManager.createNotificationChannel(reservationConfirmationChannel);
            notificationManager.createNotificationChannel(reservationReminderChannel);
        }
    }

    public static void sendNotification(Context context, String channelId, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
