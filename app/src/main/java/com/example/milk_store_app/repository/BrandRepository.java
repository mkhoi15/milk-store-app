package com.example.milk_store_app.repository;

import android.content.Context;

import com.example.milk_store_app.client.ApiClient;
import com.example.milk_store_app.services.BrandServices;

public class BrandRepository {
    public static BrandServices getBrandServices(Context context) {
        ApiClient apiClient = new ApiClient();
        return apiClient.getBrandServices(context);
    }
}
