package com.example.milk_store_app.client;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.milk_store_app.client.interceptor.AuthInterceptor;
import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.services.AuthService;
import com.example.milk_store_app.services.DeliveryServices;
import com.example.milk_store_app.services.BrandServices;
import com.example.milk_store_app.services.OrderServices;
import com.example.milk_store_app.services.ProductServices;
import com.example.milk_store_app.services.UserServices;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private Retrofit retrofit;

    private Retrofit createRetrofit(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
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

    public BrandServices getBrandServices(Context context) {
        return createRetrofit(context).create(BrandServices.class);
    }

    public UserServices getUserServices(Context context) {
        return createRetrofit(context).create(UserServices.class);
    }
    public DeliveryServices getDeliveryServices(Context context) {
        return createRetrofit(context).create(DeliveryServices.class);
    }
}
