package com.example.milk_store_app.services;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.request.LoginRequest;
import com.example.milk_store_app.models.request.RegisterRequest;
import com.example.milk_store_app.models.response.RegisterResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST(Constants.LOGIN_URL)
    Call<ResponseBody> login(@Body LoginRequest loginRequest);

    @POST(Constants.REGISTER_URL)
    Call<RegisterResponse> register(@Body RegisterRequest registerResRequest);
}
