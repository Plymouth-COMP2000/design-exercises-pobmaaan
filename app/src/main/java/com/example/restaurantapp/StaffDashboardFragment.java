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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.databinding.FragmentStaffDashboardBinding;
import com.example.restaurantapp.databinding.ItemTableStatusBinding;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.model.Table;
import com.example.restaurantapp.viewmodel.AuthViewModel;
import com.example.restaurantapp.viewmodel.ReservationViewModel;
import com.example.restaurantapp.viewmodel.TableViewModel;

import java.util.List;

public class StaffDashboardFragment extends Fragment {

    private FragmentStaffDashboardBinding binding;
    private TableViewModel tableViewModel;
    private ReservationViewModel reservationViewModel;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStaffDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tableViewModel = new ViewModelProvider(this).get(TableViewModel.class);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding.reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        authViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.textView2.setText("Good Morning, " + user.getFirstname());
            }
        });

        tableViewModel.getAllTables().observe(getViewLifecycleOwner(), tables -> {
            reservationViewModel.getAllReservations().observe(getViewLifecycleOwner(), reservations -> {
                binding.reservationsRecyclerView.setAdapter(new TableStatusAdapter(tables, reservations));
            });
        });

        binding.button4.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_reservationsFragment_to_openEnquiriesFragment));

        binding.button6.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_reservationsFragment_to_staffCreateReservationFragment);
        });

        binding.buttonUserManagement.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_reservationsFragment_to_userManagementFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class TableStatusAdapter extends RecyclerView.Adapter<TableStatusAdapter.TableStatusViewHolder> {

        private final List<Table> tables;
        private final List<Reservation> reservations;

        public TableStatusAdapter(List<Table> tables, List<Reservation> reservations) {
            this.tables = tables;
            this.reservations = reservations;
        }

        @NonNull
        @Override
        public TableStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTableStatusBinding itemBinding = ItemTableStatusBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TableStatusViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull TableStatusViewHolder holder, int position) {
            Table table = tables.get(position);
            holder.binding.tableIdTextView.setText("Table " + table.getId());

            Reservation reservationForTable = reservations.stream()
                    .filter(r -> r.getTableId() == table.getId() && "Booked".equals(r.getStatus()))
                    .findFirst()
                    .orElse(null);

            if (reservationForTable != null) {
                holder.binding.tableStatusTextView.setText("Status: Booked");
                String reservationDetails = "Name: " + reservationForTable.getCustomerName() +
                        "\nTime: " + reservationForTable.getReservationTime() +
                        "\nCovers: " + reservationForTable.getCovers();
                holder.binding.reservationDetailsTextView.setText(reservationDetails);
                holder.binding.reservationDetailsTextView.setVisibility(View.VISIBLE);
            } else {
                holder.binding.tableStatusTextView.setText("Status: Available");
                holder.binding.reservationDetailsTextView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return tables.size();
        }

        class TableStatusViewHolder extends RecyclerView.ViewHolder {
            private final ItemTableStatusBinding binding;

            public TableStatusViewHolder(ItemTableStatusBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
