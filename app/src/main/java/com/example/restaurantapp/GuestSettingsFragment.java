package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.restaurantapp.databinding.FragmentGuestSettingsBinding;
import com.example.restaurantapp.viewmodel.SettingsViewModel;

public class GuestSettingsFragment extends Fragment {

    private FragmentGuestSettingsBinding binding;
    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGuestSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        settingsViewModel.getNotificationEnabled("menu_changes").observe(getViewLifecycleOwner(), isEnabled -> {
            binding.switchMenuChanges.setChecked(isEnabled);
        });

        settingsViewModel.getNotificationEnabled("reservation_confirmation").observe(getViewLifecycleOwner(), isEnabled -> {
            binding.switchReservationConfirmation.setChecked(isEnabled);
        });

        settingsViewModel.getNotificationEnabled("reservation_changes").observe(getViewLifecycleOwner(), isEnabled -> {
            binding.switchReservationChanges.setChecked(isEnabled);
        });

        settingsViewModel.getNotificationEnabled("reservation_reminder").observe(getViewLifecycleOwner(), isEnabled -> {
            binding.switchReservationReminder.setChecked(isEnabled);
        });

        binding.switchMenuChanges.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationEnabled("menu_changes", isChecked);
        });

        binding.switchReservationConfirmation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationEnabled("reservation_confirmation", isChecked);
        });

        binding.switchReservationChanges.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationEnabled("reservation_changes", isChecked);
        });

        binding.switchReservationReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationEnabled("reservation_reminder", isChecked);
        });

        binding.buttonLogOut.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.roleSelectionFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
