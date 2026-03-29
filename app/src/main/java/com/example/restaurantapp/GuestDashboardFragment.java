package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.databinding.FragmentGuestDashboardBinding;
import com.example.restaurantapp.databinding.ItemGuestReservationBinding;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.model.User;
import com.example.restaurantapp.viewmodel.AuthViewModel;
import com.example.restaurantapp.viewmodel.ReservationViewModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GuestDashboardFragment extends Fragment {

    private FragmentGuestDashboardBinding binding;
    private ReservationViewModel reservationViewModel;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGuestDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding.reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        authViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.titleGoodMorning.setText("Good Morning, " + user.getFirstname());
                reservationViewModel.getAllReservations().observe(getViewLifecycleOwner(), reservations -> {
                    List<Reservation> guestReservations = reservations.stream()
                            .filter(r -> r.getCustomerName().equals(user.getUsername()))
                            .collect(Collectors.toList());

                    binding.titleYouHaveXBookings.setText("You have " + guestReservations.size() + " bookings:");

                    binding.reservationsRecyclerView.setAdapter(new GuestReservationsAdapter(guestReservations));
                });
            }
        });

        binding.buttonMakeNewBooking.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_guestDashboardFragment_to_createReservationFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class GuestReservationsAdapter extends RecyclerView.Adapter<GuestReservationsAdapter.GuestReservationViewHolder> {

        private final List<Reservation> reservations;

        public GuestReservationsAdapter(List<Reservation> reservations) {
            this.reservations = reservations;
        }

        @NonNull
        @Override
        public GuestReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemGuestReservationBinding itemBinding = ItemGuestReservationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new GuestReservationViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull GuestReservationViewHolder holder, int position) {
            Reservation reservation = reservations.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String reservationDetails = "Date: " + sdf.format(reservation.getDate()) +
                    "\nTime: " + reservation.getTime() +
                    "\nCovers: " + reservation.getPartySize();
            holder.binding.reservationDetailsTextView.setText(reservationDetails);

            holder.binding.editButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("reservationId", reservation.getId());
                NavHostFragment.findNavController(GuestDashboardFragment.this).navigate(R.id.action_guestDashboardFragment_to_amendBookingFragment, bundle);
            });

            holder.binding.cancelButton.setOnClickListener(v -> {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to cancel this booking?")
                        .setPositiveButton("Cancel", (dialog, which) -> {
                            reservationViewModel.delete(reservation);
                        })
                        .setNegativeButton("Back", null)
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            return reservations.size();
        }

        class GuestReservationViewHolder extends RecyclerView.ViewHolder {
            private final ItemGuestReservationBinding binding;

            public GuestReservationViewHolder(ItemGuestReservationBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
