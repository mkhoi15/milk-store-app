package com.example.milk_store_app.services;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.request.PostOrderRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OrderServices {
    @POST(Constants.ORDER_URL)
    Call<PostOrderRequest> createOrder(@Body PostOrderRequest request);
}
