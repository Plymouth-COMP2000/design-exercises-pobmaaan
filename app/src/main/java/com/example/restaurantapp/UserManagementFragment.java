package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.restaurantapp.databinding.FragmentUserManagementBinding;

public class UserManagementFragment extends Fragment {

    private FragmentUserManagementBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonGuests.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("user_type", "Guest");
            NavHostFragment.findNavController(this).navigate(R.id.action_userManagementFragment_to_userListFragment, args);
        });

        binding.buttonStaff.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("user_type", "Staff");
            NavHostFragment.findNavController(this).navigate(R.id.action_userManagementFragment_to_userListFragment, args);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
