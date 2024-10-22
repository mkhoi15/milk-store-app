package com.example.milk_store_app.repository;

import android.content.Context;

import com.example.milk_store_app.client.ApiClient;
import com.example.milk_store_app.services.OrderServices;

public class OrderRepository {
    public static OrderServices getOrderServices(Context context) {
        ApiClient apiClient = new ApiClient();
        return apiClient.getOrderServices(context);
    }
}
