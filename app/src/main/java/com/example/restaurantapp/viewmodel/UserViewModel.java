package com.example.restaurantapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.restaurantapp.api.ApiHelper;
import com.example.restaurantapp.model.MessageResponse;
import com.example.restaurantapp.model.User;
import com.example.restaurantapp.model.UserResponse;
import com.example.restaurantapp.model.UsersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private final ApiHelper apiHelper = new ApiHelper();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<User>> getAllUsers() {
        apiHelper.getAllUsers().enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    users.setValue(response.body().getUsers());
                } else {
                    error.setValue("Failed to fetch users");
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return users;
    }

    public LiveData<User> getUser(String username) {
        apiHelper.getUser(username).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user.setValue(response.body().getUser());
                } else {
                    error.setValue("Failed to fetch user");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return user;
    }

    public void createUser(User newUser) {
        apiHelper.createUser(newUser).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    getAllUsers();
                } else {
                    error.setValue("Failed to create user");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    public void updateUser(String username, User updatedUser) {
        apiHelper.updateUser(username, updatedUser).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    getAllUsers();
                } else {
                    error.setValue("Failed to update user");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    public void deleteUser(String username) {
        apiHelper.deleteUser(username).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    getAllUsers();
                } else {
                    error.setValue("Failed to delete user");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }
}
