package com.example.restaurantapp;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager.createNotificationChannels(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            final int destinationId = destination.getId();
            if (destinationId == R.id.roleSelectionFragment ||
                destinationId == R.id.staffLoginFragment ||
                destinationId == R.id.guestLoginFragment ||
                destinationId == R.id.guestSignUpFragment) {
                bottomNavView.setVisibility(View.GONE);
            } else if (destinationId == R.id.guestDashboardFragment) {
                bottomNavView.setVisibility(View.VISIBLE);
                bottomNavView.getMenu().clear();
                bottomNavView.inflateMenu(R.menu.guest_bottom_nav_menu);
                NavigationUI.setupWithNavController(bottomNavView, navController);
            } else if (destinationId == R.id.reservationsFragment) { // Staff Dashboard
                bottomNavView.setVisibility(View.VISIBLE);
                bottomNavView.getMenu().clear();
                bottomNavView.inflateMenu(R.menu.staff_bottom_nav_menu);
                NavigationUI.setupWithNavController(bottomNavView, navController);
            } else {
                if (bottomNavView.getVisibility() == View.GONE) {
                    bottomNavView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
