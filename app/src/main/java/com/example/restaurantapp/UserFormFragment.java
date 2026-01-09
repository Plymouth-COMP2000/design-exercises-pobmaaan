package com.example.restaurantapp;

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

import com.example.restaurantapp.databinding.FragmentUserFormBinding;
import com.example.restaurantapp.model.User;
import com.example.restaurantapp.viewmodel.UserViewModel;

public class UserFormFragment extends Fragment {

    private FragmentUserFormBinding binding;
    private UserViewModel userViewModel;
    private String userType;
    private String username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userType = getArguments().getString("user_type");
            username = getArguments().getString("username");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (username != null) {
            userViewModel.getUser(username).observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    binding.editTextUsername.setText(user.getUsername());
                    binding.editTextPassword.setText(user.getPassword());
                    binding.editTextFirstName.setText(user.getFirstname());
                    binding.editTextLastName.setText(user.getLastname());
                    binding.editTextEmail.setText(user.getEmail());
                    binding.editTextContact.setText(user.getContact());
                }
            });
        }

        binding.buttonSave.setOnClickListener(v -> {
            String newUsername = binding.editTextUsername.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String firstName = binding.editTextFirstName.getText().toString();
            String lastName = binding.editTextLastName.getText().toString();
            String email = binding.editTextEmail.getText().toString();
            String contact = binding.editTextContact.getText().toString();

            if (newUsername.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(newUsername, password, firstName, lastName, email, contact, userType);

            if (username != null) {
                userViewModel.updateUser(username, user);
            } else {
                userViewModel.createUser(user);
            }
            NavHostFragment.findNavController(this).popBackStack();
        });
    }
}
