package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.restaurantapp.databinding.FragmentRoleSelectionBinding;

public class RoleSelectionFragment extends Fragment {

    private FragmentRoleSelectionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoleSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonStaff.setOnClickListener(v -> 
            NavHostFragment.findNavController(this).navigate(R.id.action_roleSelectionFragment_to_staffLoginFragment));

        binding.buttonLogIn.setOnClickListener(v -> 
            NavHostFragment.findNavController(this).navigate(R.id.action_roleSelectionFragment_to_guestLoginFragment));

        binding.buttonSignUp.setOnClickListener(v -> 
            NavHostFragment.findNavController(this).navigate(R.id.action_roleSelectionFragment_to_guestSignUpFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
