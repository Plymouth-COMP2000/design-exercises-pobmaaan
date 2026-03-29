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
import com.example.restaurantapp.databinding.FragmentMenuDashboardBinding;
import com.example.restaurantapp.databinding.ItemMenuCategoryBinding;
import com.example.restaurantapp.databinding.ItemMenuItemBinding;
import com.example.restaurantapp.model.MenuCategory;
import com.example.restaurantapp.model.MenuItem;
import com.example.restaurantapp.model.MenuItemDetails;
import com.example.restaurantapp.model.MenuItemSealed;
import com.example.restaurantapp.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuDashboardFragment extends Fragment {

    private FragmentMenuDashboardBinding binding;
    private MenuViewModel menuViewModel;
    private MenuAdapter adapter;
    private List<MenuItem> menuItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        adapter = new MenuAdapter(new ArrayList<>());
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        menuViewModel.getAllMenuItems().observe(getViewLifecycleOwner(), items -> {
            this.menuItems = items;
            updateMenuList(items);
        });

        binding.buttonAddMenuItem.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_addMenuItemFragment));
    }

    private void updateMenuList(List<MenuItem> menuItems) {
        Map<String, List<MenuItem>> groupedMenu = new LinkedHashMap<>();
        List<String> desiredOrder = Arrays.asList("Starter", "Main", "Dessert", "Side", "Drink");

        for (String category : desiredOrder) {
            groupedMenu.put(category, new ArrayList<>());
        }

        for (MenuItem item : menuItems) {
            if (item.getCategory().equals("Main Course")) {
                item.setCategory("Main");
            }
            if (!groupedMenu.containsKey(item.getCategory())) {
                groupedMenu.put(item.getCategory(), new ArrayList<>());
            }
            groupedMenu.get(item.getCategory()).add(item);
        }

        List<MenuItemSealed> sealedMenuItems = new ArrayList<>();
        for (Map.Entry<String, List<MenuItem>> entry : groupedMenu.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                sealedMenuItems.add(new MenuCategory(entry.getKey()));
                for (MenuItem item : entry.getValue()) {
                    sealedMenuItems.add(new MenuItemDetails(item.getId(), item.getName(), item.getAllergens(), item.getDescription(), "£" + item.getPrice()));
                }
            }
        }
        adapter.updateData(sealedMenuItems);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<MenuItemSealed> items;

        public MenuAdapter(List<MenuItemSealed> items) {
            this.items = items;
        }

        public void updateData(List<MenuItemSealed> newItems) {
            items = newItems;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (items.get(position) instanceof MenuCategory) {
                return 0;
            } else {
                return 1;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                ItemMenuCategoryBinding categoryBinding = ItemMenuCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new CategoryViewHolder(categoryBinding);
            } else {
                ItemMenuItemBinding itemBinding = ItemMenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ItemViewHolder(itemBinding);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MenuItemSealed item = items.get(position);
            if (holder.getItemViewType() == 0) {
                ((CategoryViewHolder) holder).bind((MenuCategory) item);
            } else {
                ((ItemViewHolder) holder).bind((MenuItemDetails) item);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {
            private final ItemMenuCategoryBinding binding;

            public CategoryViewHolder(ItemMenuCategoryBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(MenuCategory category) {
                binding.textViewCategoryName.setText(category.getName());
            }
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private final ItemMenuItemBinding binding;

            public ItemViewHolder(ItemMenuItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(MenuItemDetails item) {
                binding.textViewItemName.setText(item.getName());
                binding.textViewItemAllergens.setText(item.getAllergens());
                binding.textViewItemDetails.setText(item.getDetails());
                binding.textViewItemPrice.setText(item.getPrice());

                binding.buttonEditItem.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("menuItemId", item.getId());
                    NavHostFragment.findNavController(MenuDashboardFragment.this).navigate(R.id.action_menuFragment_to_amendMenuItemFragment, bundle);
                });

                binding.buttonDeleteItem.setOnClickListener(v ->
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Delete Menu Item")
                                .setMessage("Are you sure you want to delete this menu item?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    for (MenuItem menuItem : menuItems) {
                                        if (menuItem.getId() == item.getId()) {
                                            menuViewModel.delete(menuItem);
                                            break;
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show());
            }
        }
    }
}
