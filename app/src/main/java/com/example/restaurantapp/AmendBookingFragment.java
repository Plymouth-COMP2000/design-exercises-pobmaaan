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
import com.example.restaurantapp.databinding.FragmentAmendBookingBinding;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.model.Table;
import com.example.restaurantapp.viewmodel.ReservationViewModel;
import com.example.restaurantapp.viewmodel.SettingsViewModel;
import com.example.restaurantapp.viewmodel.TableViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AmendBookingFragment extends Fragment {

    private FragmentAmendBookingBinding binding;
    private ReservationViewModel reservationViewModel;
    private TableViewModel tableViewModel;
    private SettingsViewModel settingsViewModel;
    private Reservation currentReservation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAmendBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        tableViewModel = new ViewModelProvider(this).get(TableViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setupTimeSpinner();

        int reservationId = getArguments().getInt("reservationId");

        reservationViewModel.getAllReservations().observe(getViewLifecycleOwner(), reservations -> {
            for (Reservation reservation : reservations) {
                if (reservation.getId() == reservationId) {
                    currentReservation = reservation;
                    break;
                }
            }

            if (currentReservation != null) {
                binding.editTextName.setText(currentReservation.getName());
                binding.editTextCovers.setText(String.valueOf(currentReservation.getPartySize()));
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.spinnerTime.getAdapter();
                int position = adapter.getPosition(currentReservation.getTime());
                binding.spinnerTime.setSelection(position);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                binding.editTextDate.setText(sdf.format(currentReservation.getDate()));
            }
        });

        binding.buttonClose.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        binding.buttonConfirmAmend.setOnClickListener(v -> {
            String name = binding.editTextName.getText().toString();
            String coversStr = binding.editTextCovers.getText().toString();
            String time = binding.spinnerTime.getSelectedItem().toString();
            String dateStr = binding.editTextDate.getText().toString();

            if (!name.isEmpty() && !coversStr.isEmpty() && !time.isEmpty() && !dateStr.isEmpty()) {
                try {
                    int covers = Integer.parseInt(coversStr);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = sdf.parse(dateStr);

                    tableViewModel.getAllTables().observe(getViewLifecycleOwner(), tables -> {
                        Table availableTable = null;
                        for (Table table : tables) {
                            if (table.getSeats() >= covers) {
                                availableTable = table;
                                break;
                            }
                        }

                        if (availableTable != null) {
                            currentReservation.setName(name);
                            currentReservation.setPartySize(covers);
                            currentReservation.setTime(time);
                            currentReservation.setDate(date);
                            currentReservation.setTableId(availableTable.getId());
                            reservationViewModel.update(currentReservation);
                            settingsViewModel.getNotificationEnabled("reservation_changes").observe(getViewLifecycleOwner(), isEnabled -> {
                                if (isEnabled) {
                                    NotificationManager.sendNotification(
                                            requireContext(),
                                            "reservation_changes",
                                            "Reservation Updated",
                                            "Your reservation for " + name + " has been updated."
                                    );
                                }
                            });
                            NavHostFragment.findNavController(this).popBackStack();
                        } else {
                            Toast.makeText(requireContext(), "No available table for this party size", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Please enter valid details", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
