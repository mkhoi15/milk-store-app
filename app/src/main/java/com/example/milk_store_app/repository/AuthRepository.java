package com.example.milk_store_app.repository;

import android.content.Context;

import com.example.milk_store_app.client.ApiClient;
import com.example.milk_store_app.services.AuthService;

public final class AuthRepository {

    public static AuthService getAuthService(Context context) {
        ApiClient apiClient = new ApiClient();
        return apiClient.getAuthService(context);
    }
}
