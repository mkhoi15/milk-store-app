package com.example.milk_store_app.repository;

import android.content.Context;

import com.example.milk_store_app.client.ApiClient;
import com.example.milk_store_app.services.ProductServices;
import com.example.milk_store_app.services.UserService;

public class UserRepository {
    public static UserService getUserService(Context context) {
        ApiClient apiClient = new ApiClient();
        return apiClient.getUserServices(context);
    }
}
