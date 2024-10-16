package com.example.milk_store_app.services;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.request.LoginRequest;
import com.example.milk_store_app.models.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST(Constants.LOGIN_URL)
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
