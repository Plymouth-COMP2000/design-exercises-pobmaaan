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
import com.example.restaurantapp.databinding.FragmentAmendMenuItemBinding;
import com.example.restaurantapp.model.MenuItem;
import com.example.restaurantapp.viewmodel.MenuViewModel;
import com.example.restaurantapp.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class AmendMenuItemFragment extends Fragment {

    private FragmentAmendMenuItemBinding binding;
    private MenuViewModel menuViewModel;
    private SettingsViewModel settingsViewModel;
    private MenuItem currentMenuItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAmendMenuItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        int menuItemId = getArguments().getInt("menuItemId");

        menuViewModel.getAllMenuItems().observe(getViewLifecycleOwner(), menuItems -> {
            for (MenuItem item : menuItems) {
                if (item.getId() == menuItemId) {
                    currentMenuItem = item;
                    break;
                }
            }
            if (currentMenuItem != null) {
                binding.editTextName.setText(currentMenuItem.getName());
                binding.editTextPrice.setText(String.valueOf(currentMenuItem.getPrice()));
                binding.editTextAllergens.setText(currentMenuItem.getAllergens());
                binding.editTextDetails.setText(currentMenuItem.getDescription());
                binding.autoCompleteCategory.setText(currentMenuItem.getCategory());
            }

            Set<String> categories = new LinkedHashSet<>();
            categories.add("Starter");
            categories.add("Main");
            categories.add("Dessert");
            categories.add("Side");
            categories.add("Drink");
            for (MenuItem item : menuItems) {
                categories.add(item.getCategory());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>(categories));
            binding.autoCompleteCategory.setAdapter(adapter);
        });

        binding.buttonClose.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        binding.buttonSaveChanges.setOnClickListener(v -> {
            String name = binding.editTextName.getText().toString();
            String priceStr = binding.editTextPrice.getText().toString();
            String allergens = binding.editTextAllergens.getText().toString();
            String details = binding.editTextDetails.getText().toString();
            String category = binding.autoCompleteCategory.getText().toString();

            if (!name.isEmpty() && !priceStr.isEmpty() && !allergens.isEmpty() && !details.isEmpty() && !category.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr);
                    currentMenuItem.setName(name);
                    currentMenuItem.setPrice(price);
                    currentMenuItem.setAllergens(allergens);
                    currentMenuItem.setDescription(details);
                    currentMenuItem.setCategory(category);
                    menuViewModel.update(currentMenuItem);
                    settingsViewModel.getNotificationEnabled("menu_changes").observe(getViewLifecycleOwner(), isEnabled -> {
                        if (isEnabled) {
                            NotificationManager.sendNotification(
                                    requireContext(),
                                    "menu_changes",
                                    "Menu Updated",
                                    "The item " + name + " has been updated."
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
