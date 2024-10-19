package com.example.milk_store_app.repository;

import android.content.Context;

import com.example.milk_store_app.client.ApiClient;
import com.example.milk_store_app.services.ProductServices;

public class ProductRepository {
    public static ProductServices getProductServices(Context context) {
        ApiClient apiClient = new ApiClient();
        return apiClient.getProductServices(context);
    }
}
