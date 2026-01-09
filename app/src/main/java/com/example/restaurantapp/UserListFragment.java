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
import com.example.restaurantapp.databinding.FragmentUserListBinding;
import com.example.restaurantapp.databinding.ItemUserBinding;
import com.example.restaurantapp.model.User;
import com.example.restaurantapp.viewmodel.UserViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class UserListFragment extends Fragment {

    private FragmentUserListBinding binding;
    private UserViewModel userViewModel;
    private String userType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userType = getArguments().getString("user_type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.usersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getUsertype().equals(userType))
                    .collect(Collectors.toList());
            binding.usersRecyclerView.setAdapter(new UserAdapter(filteredUsers));
        });

        binding.buttonCreate.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("user_type", userType);
            NavHostFragment.findNavController(this).navigate(R.id.action_userListFragment_to_userFormFragment, args);
        });
    }

    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

        private final List<User> users;

        public UserAdapter(List<User> users) {
            this.users = users;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemUserBinding itemBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new UserViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = users.get(position);
            holder.binding.userName.setText(user.getFirstname() + " " + user.getLastname());

            holder.binding.buttonEdit.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("user_type", userType);
                args.putString("username", user.getUsername());
                NavHostFragment.findNavController(UserListFragment.this).navigate(R.id.action_userListFragment_to_userFormFragment, args);
            });

            holder.binding.buttonDelete.setOnClickListener(v -> {
                userViewModel.deleteUser(user.getUsername());
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            private final ItemUserBinding binding;

            public UserViewHolder(ItemUserBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
