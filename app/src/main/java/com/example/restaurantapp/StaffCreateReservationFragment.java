package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.restaurantapp.databinding.FragmentStaffCreateReservationBinding;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.viewmodel.ReservationViewModel;
import com.example.restaurantapp.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StaffCreateReservationFragment extends Fragment {

    private FragmentStaffCreateReservationBinding binding;
    private ReservationViewModel reservationViewModel;
    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStaffCreateReservationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setupTimeSpinner();

        binding.buttonClose.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.buttonSelectTable.setOnClickListener(v -> {
            String name = binding.editTextName.getText().toString();
            String coversStr = binding.editTextCovers.getText().toString();
            String time = binding.spinnerTime.getSelectedItem().toString();

            if (!name.isEmpty() && !coversStr.isEmpty() && !time.isEmpty()) {
                try {
                    int covers = Integer.parseInt(coversStr);
                    Reservation reservation = new Reservation(
                            name,
                            time,
                            covers,
                            -1,
                            new Date(),
                            "Pending"
                    );
                    reservationViewModel.insert(reservation);
                    settingsViewModel.getNotificationEnabled("new_reservation").observe(getViewLifecycleOwner(), isEnabled -> {
                        if (isEnabled) {
                            NotificationManager.sendNotification(
                                    requireContext(),
                                    "new_reservation",
                                    "New Reservation",
                                    "A new reservation has been created for " + name
                            );
                        }
                    });
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_staffCreateReservationFragment_to_selectTableFragment, createArgs(reservation));
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Please enter a valid number for covers", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTimeSpinner() {
        List<String> timeSlots = new ArrayList<>();
        for (int h = 12; h <= 14; h++) {
            for (int m = 0; m < 60; m += 15) {
                if (h == 14 && m > 30) continue;
                timeSlots.add(String.format("%02d:%02d", h, m));
            }
        }
        for (int h = 17; h <= 20; h++) {
            for (int m = 0; m < 60; m += 15) {
                if (h == 20 && m > 30) continue;
                timeSlots.add(String.format("%02d:%02d", h, m));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTime.setAdapter(adapter);
    }

    public static Bundle createArgs(Reservation reservation) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("reservation", reservation);
        return bundle;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
