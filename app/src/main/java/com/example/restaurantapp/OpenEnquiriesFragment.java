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
import com.example.restaurantapp.databinding.FragmentOpenEnquiriesBinding;
import com.example.restaurantapp.databinding.ItemOpenEnquiryBinding;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.viewmodel.ReservationViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class OpenEnquiriesFragment extends Fragment {

    private FragmentOpenEnquiriesBinding binding;
    private ReservationViewModel reservationViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOpenEnquiriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        binding.enquiriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        reservationViewModel.getAllReservations().observe(getViewLifecycleOwner(), reservations -> {
            List<Reservation> pendingReservations = reservations.stream()
                    .filter(r -> "Pending".equals(r.getStatus()))
                    .collect(Collectors.toList());
            binding.enquiriesRecyclerView.setAdapter(new EnquiriesAdapter(pendingReservations));
        });

        binding.buttonClose.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class EnquiriesAdapter extends RecyclerView.Adapter<EnquiriesAdapter.EnquiryViewHolder> {

        private final List<Reservation> enquiries;

        public EnquiriesAdapter(List<Reservation> enquiries) {
            this.enquiries = enquiries;
        }

        @NonNull
        @Override
        public EnquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemOpenEnquiryBinding itemBinding = ItemOpenEnquiryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new EnquiryViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull EnquiryViewHolder holder, int position) {
            Reservation enquiry = enquiries.get(position);
            String reservationDetails = "Name: " + enquiry.getCustomerName() +
                    "\nTime: " + enquiry.getReservationTime() +
                    "\nCovers: " + enquiry.getCovers();
            holder.binding.enquiryDetailsTextView.setText(reservationDetails);

            holder.binding.acceptButton.setOnClickListener(v -> {
                NavHostFragment.findNavController(OpenEnquiriesFragment.this)
                        .navigate(R.id.action_openEnquiriesFragment_to_selectTableFragment, OpenEnquiriesFragment.createArgs(enquiry));
            });

            holder.binding.declineButton.setOnClickListener(v ->
                new AlertDialog.Builder(requireContext())
                    .setTitle("Decline Enquiry")
                    .setMessage("Are you sure you want to decline this enquiry?")
                    .setPositiveButton("Decline", (dialog, which) -> {
                        reservationViewModel.delete(enquiry);
                    })
                    .setNegativeButton("Cancel", null)
                    .show());
        }

        @Override
        public int getItemCount() {
            return enquiries.size();
        }

        class EnquiryViewHolder extends RecyclerView.ViewHolder {
            private final ItemOpenEnquiryBinding binding;

            public EnquiryViewHolder(ItemOpenEnquiryBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    public static Bundle createArgs(Reservation reservation) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("reservation", reservation);
        return bundle;
    }
}
