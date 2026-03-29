package com.example.restaurantapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.restaurantapp.databinding.FragmentSelectTableBinding;
import com.example.restaurantapp.databinding.ItemTableBinding;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.model.Table;
import com.example.restaurantapp.viewmodel.ReservationViewModel;
import com.example.restaurantapp.viewmodel.SettingsViewModel;
import com.example.restaurantapp.viewmodel.TableViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SelectTableFragment extends Fragment {

    private FragmentSelectTableBinding binding;
    private ReservationViewModel reservationViewModel;
    private TableViewModel tableViewModel;
    private SettingsViewModel settingsViewModel;
    private Reservation reservation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservation = getArguments().getParcelable("reservation");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectTableBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        tableViewModel = new ViewModelProvider(this).get(TableViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding.reservationDetails.setText("Reservation for " + reservation.getName() + " at " + reservation.getTime() + " for " + reservation.getPartySize() + " people");

        binding.tablesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        tableViewModel.getAllTables().observe(getViewLifecycleOwner(), tables -> {
            reservationViewModel.getAllReservations().observe(getViewLifecycleOwner(), reservations -> {
                List<Table> availableTables = tables.stream()
                        .filter(table -> isTableAvailable(table, reservation, reservations))
                        .collect(Collectors.toList());
                binding.tablesRecyclerView.setAdapter(new TablesAdapter(availableTables));
            });
        });
    }

    private boolean isTableAvailable(Table table, Reservation newReservation, List<Reservation> allReservations) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            long newReservationTime = sdf.parse(newReservation.getTime()).getTime();
            long timeWindow = 90 * 60 * 1000; // 1.5 hours in milliseconds

            for (Reservation existingReservation : allReservations) {
                if (existingReservation.getTableId() == table.getId()) {
                    long existingReservationTime = sdf.parse(existingReservation.getTime()).getTime();
                    if (Math.abs(newReservationTime - existingReservationTime) < timeWindow) {
                        return false;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private class TablesAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<TablesAdapter.TableViewHolder> {

        private final List<Table> tables;

        public TablesAdapter(List<Table> tables) {
            this.tables = tables;
        }

        @NonNull
        @Override
        public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTableBinding itemBinding = ItemTableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TableViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
            Table table = tables.get(position);
            holder.binding.tableIdTextView.setText("Table " + table.getId());
            holder.binding.tableCapacityTextView.setText("Capacity: " + table.getSeats());

            holder.binding.selectTableButton.setOnClickListener(v -> {
                reservation.setTableId(table.getId());
                reservation.setStatus("Booked");
                reservationViewModel.update(reservation);
                settingsViewModel.getNotificationEnabled("reservation_confirmation").observe(getViewLifecycleOwner(), isEnabled -> {
                    if (isEnabled) {
                        NotificationManager.sendNotification(
                                requireContext(),
                                "reservation_confirmation",
                                "Reservation Confirmed",
                                "Your reservation for " + reservation.getName() + " at " + reservation.getTime() + " is confirmed."
                        );
                    }
                });

                settingsViewModel.getNotificationEnabled("reservation_reminder").observe(getViewLifecycleOwner(), isEnabled -> {
                    if (isEnabled) {
                        scheduleReminder(reservation);
                    }
                });

                Toast.makeText(requireContext(), "Reservation confirmed for table " + table.getId(), Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(SelectTableFragment.this).popBackStack();
            });
        }

        private void scheduleReminder(Reservation reservation) {
            try {
                AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(requireContext(), NotificationReceiver.class);
                intent.putExtra("reservation_name", reservation.getName());
                intent.putExtra("reservation_time", reservation.getTime());

                PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), reservation.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(reservation.getTime()));
                calendar.add(Calendar.HOUR, -1);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return tables.size();
        }

        class TableViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            private final ItemTableBinding binding;

            public TableViewHolder(ItemTableBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
