package com.example.milk_store_app.repository;

import android.content.Context;

import com.example.milk_store_app.client.ApiClient;
import com.example.milk_store_app.services.DeliveryServices;

public class DeliveryRepository {
    public static DeliveryServices getDeliveryServices(Context context) {
        ApiClient apiClient = new ApiClient();
        return apiClient.getDeliveryServices(context);
    }
}
