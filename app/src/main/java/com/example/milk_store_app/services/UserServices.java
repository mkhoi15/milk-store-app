package com.example.milk_store_app.services;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.response.UserResponse;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserServices {
    @GET(Constants.USER_URL + "/{id}")
    Call<UserResponse> getUserById(@Path("id") UUID id);
}
