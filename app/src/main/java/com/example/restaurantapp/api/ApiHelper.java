package com.example.restaurantapp.api;

import com.example.restaurantapp.model.MessageResponse;
import com.example.restaurantapp.model.User;
import com.example.restaurantapp.model.UserResponse;
import com.example.restaurantapp.model.UsersResponse;

import retrofit2.Call;

public class ApiHelper {

    private final ApiService apiService = RetrofitClient.getInstance().getApiService();
    private final String studentId = "10906485";

    public Call<MessageResponse> createStudentDatabase() {
        return apiService.createStudentDatabase(studentId);
    }

    public Call<MessageResponse> createUser(User user) {
        return apiService.createUser(studentId, user);
    }

    public Call<UsersResponse> getAllUsers() {
        return apiService.getAllUsers(studentId);
    }

    public Call<UserResponse> getUser(String username) {
        return apiService.getUser(studentId, username);
    }

    public Call<MessageResponse> updateUser(String username, User user) {
        return apiService.updateUser(studentId, username, user);
    }

    public Call<MessageResponse> deleteUser(String username) {
        return apiService.deleteUser(studentId, username);
    }
}
