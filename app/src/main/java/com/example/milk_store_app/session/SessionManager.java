package com.example.milk_store_app.session;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.milk_store_app.R;

public class SessionManager {
    private final SharedPreferences sharedPreferences;
    private static final String USER_TOKEN = "user_token";

    public SessionManager(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    /**
     * Function to save auth token
     */
    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    /**
     * Function to fetch auth token from shared preferences
     */
    public String fetchAuthToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }
}
