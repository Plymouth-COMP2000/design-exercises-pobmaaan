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
import com.example.restaurantapp.databinding.FragmentStaffSettingsBinding;
import com.example.restaurantapp.viewmodel.SettingsViewModel;

public class StaffSettingsFragment extends Fragment {

    private FragmentStaffSettingsBinding binding;
    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStaffSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        settingsViewModel.getNotificationEnabled("menu_changes").observe(getViewLifecycleOwner(), isEnabled -> {
            binding.switchMenuChanges.setChecked(isEnabled);
        });

        settingsViewModel.getNotificationEnabled("new_reservation").observe(getViewLifecycleOwner(), isEnabled -> {
            binding.switchNewReservation.setChecked(isEnabled);
        });

        settingsViewModel.getNotificationEnabled("reservation_changes").observe(getViewLifecycleOwner(), isEnabled -> {
            binding.switchReservationChanges.setChecked(isEnabled);
        });

        binding.switchMenuChanges.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationEnabled("menu_changes", isChecked);
        });

        binding.switchNewReservation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationEnabled("new_reservation", isChecked);
        });

        binding.switchReservationChanges.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationEnabled("reservation_changes", isChecked);
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
