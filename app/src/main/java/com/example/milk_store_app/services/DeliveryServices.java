package com.example.milk_store_app.services;

import androidx.annotation.NonNull;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.response.DeliveryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeliveryServices {

    @GET(Constants.DELIVERY_URL + "/user/{id}")
    Call<List<DeliveryResponse>> getDeliveriesByUserId(
            @Path("id") @NonNull String userId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );
}
