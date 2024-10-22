package com.example.milk_store_app.client;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.milk_store_app.client.interceptor.AuthInterceptor;
import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.services.AuthService;
import com.example.milk_store_app.services.OrderServices;
import com.example.milk_store_app.services.ProductServices;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private Retrofit retrofit;

    private Retrofit createRetrofit(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient(context))
                .build();
            return retrofit;
        }
        return retrofit;
    }

    @NonNull
    private OkHttpClient createOkHttpClient(Context context) {
        return new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor(context))
            .build();
    }

    public AuthService getAuthService(Context context) {
        return createRetrofit(context).create(AuthService.class);
    }

    public ProductServices getProductServices(Context context) {
        return createRetrofit(context).create(ProductServices.class);
    }

    public OrderServices getOrderServices(Context context) {
        return createRetrofit(context).create(OrderServices.class);
    }
}
