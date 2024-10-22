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
    private static final String USER_ID = "user_id";

    public SessionManager(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    /**
     * Function to save auth token
     */
    public void saveAuthToken(String token) {
        JWT jwt = new JWT(token);
        String role = jwt.getClaim(JwtConstants.USER_ROLE).asString();
        String userId = jwt.getClaim(JwtConstants.USER_ID).asString();
        String nameIdentifier = jwt.getClaim(JwtConstants.NAME_IDENTIFIER).asString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.putString(USER_ROLE, role);
        editor.putString(NAME_IDENTIFIER, nameIdentifier);
        editor.putString(USER_ID, userId);
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

    /**
     * Function to fetch user id from shared preferences
     */
    public String fetchUserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public boolean isLoggedIn() {
        return fetchAuthToken() != null;
    }


    public boolean isAdmin() {
        return isRole("Admin");
    }

    public boolean isShopStaff() {
        return isRole("ShopStaff");
    }

    public boolean isDeliveryStaff() {
        return isRole("DeliveryStaff");
    }

    public boolean isCustomer() {
        return isRole("Customer");
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private boolean isRole(@NonNull String role) {
        return role.equals(fetchUserRole());
    }
}

