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
import com.example.restaurantapp.databinding.FragmentAddMenuItemBinding;
import com.example.restaurantapp.model.MenuItem;
import com.example.restaurantapp.viewmodel.MenuViewModel;
import com.example.restaurantapp.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class AddMenuItemFragment extends Fragment {

    private FragmentAddMenuItemBinding binding;
    private MenuViewModel menuViewModel;
    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddMenuItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        menuViewModel.getAllMenuItems().observe(getViewLifecycleOwner(), menuItems -> {
            Set<String> categories = new LinkedHashSet<>();
            categories.add("Starter");
            categories.add("Main");
            categories.add("Dessert");
            categories.add("Side");
            categories.add("Drink");
            if (menuItems != null) {
                for (MenuItem item : menuItems) {
                    if (item.getCategory().equals("Main Course")) {
                        item.setCategory("Main");
                    }
                    categories.add(item.getCategory());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>(categories));
            binding.autoCompleteCategory.setAdapter(adapter);
        });

        binding.buttonClose.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.buttonSaveItem.setOnClickListener(v -> {
            String name = binding.editTextName.getText().toString();
            String priceStr = binding.editTextPrice.getText().toString();
            String allergens = binding.editTextAllergens.getText().toString();
            String details = binding.editTextDetails.getText().toString();
            String category = binding.autoCompleteCategory.getText().toString();

            if (!name.isEmpty() && !priceStr.isEmpty() && !allergens.isEmpty() && !details.isEmpty() && !category.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr);
                    MenuItem menuItem = new MenuItem(
                            name,
                            details,
                            price,
                            category,
                            allergens
                    );
                    menuViewModel.insert(menuItem);
                    settingsViewModel.getNotificationEnabled("menu_changes").observe(getViewLifecycleOwner(), isEnabled -> {
                        if (isEnabled) {
                            NotificationManager.sendNotification(
                                    requireContext(),
                                    "menu_changes",
                                    "Menu Updated",
                                    "A new item has been added: " + name
                            );
                        }
                    });
                    NavHostFragment.findNavController(this).popBackStack();
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Please enter a valid price", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
