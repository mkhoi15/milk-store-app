package com.example.milk_store_app.client.interceptor;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.milk_store_app.session.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        sessionManager = new SessionManager(context);
    }


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String token = sessionManager.fetchAuthToken();
        Request.Builder builder = chain.request().newBuilder();
        if (token != null) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(builder.build());
    }
}
