package com.example.restaurantapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.restaurantapp.api.ApiHelper;
import com.example.restaurantapp.model.MessageResponse;
import com.example.restaurantapp.model.User;
import com.example.restaurantapp.model.UserResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {

    private final ApiHelper apiHelper = new ApiHelper();
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void login(String username, String password) {
        apiHelper.getUser(username).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User fetchedUser = response.body().getUser();
                    if (fetchedUser.getPassword().equals(password)) {
                        user.setValue(fetchedUser);
                    } else {
                        error.setValue("Invalid password");
                    }
                } else {
                    error.setValue("User not found");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    public void signUp(User newUser) {
        apiHelper.createStudentDatabase().enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                apiHelper.createUser(newUser).enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.isSuccessful()) {
                            user.setValue(newUser);
                        } else {
                            String errorMessage = "Sign-up failed";
                            if (response.errorBody() != null) {
                                try {
                                    errorMessage = "Sign-up failed: " + response.errorBody().string();
                                } catch (IOException e) {

                                }
                            }
                            error.setValue(errorMessage);
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        error.setValue("Sign-up failed: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                error.setValue("Failed to connect to server: " + t.getMessage());
            }
        });
    }
}
