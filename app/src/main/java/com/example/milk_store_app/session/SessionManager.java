package com.example.milk_store_app.session;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.auth0.android.jwt.JWT;
import com.example.milk_store_app.R;
import com.example.milk_store_app.constants.JwtConstants;

public class SessionManager {
    private final SharedPreferences sharedPreferences;
    private static final String USER_TOKEN = "user_token";
    private static final String USER_ROLE = "user_role";
    private static final String NAME_IDENTIFIER = "name_identifier";

    public SessionManager(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    /**
     * Function to save auth token
     */
    public void saveAuthToken(String token) {
        JWT jwt = new JWT(token);
        String role = jwt.getClaim(JwtConstants.USER_ROLE).asString();
        String nameIdentifier = jwt.getClaim(JwtConstants.NAME_IDENTIFIER).asString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.putString(USER_ROLE, role);
        editor.putString(NAME_IDENTIFIER, nameIdentifier);
        editor.apply();
    }

    /**
     * Function to fetch auth token from shared preferences
     */
    public String fetchAuthToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    /**
     * Function to fetch user role from shared preferences
     */
    public String fetchUserRole() {
        return sharedPreferences.getString(USER_ROLE, null);
    }

    /**
     * Function to fetch name identifier from shared preferences
     */
    public String fetchNameIdentifier() {
        return sharedPreferences.getString(NAME_IDENTIFIER, null);
    }
}
