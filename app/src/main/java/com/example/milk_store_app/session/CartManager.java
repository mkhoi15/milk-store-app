package com.example.milk_store_app.session;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.milk_store_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class CartManager {
    private static final String CART_KEY = "cart";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public CartManager(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        gson = new Gson();  // Gson instance for serialization
    }

    // Add or update an item in the cart
    public void addItemToCart(String productId, int quantity) {
        HashMap<String, Integer> cart = getCart();
        cart.put(productId, quantity);
        saveCart(cart);  // Save updated cart back to SharedPreferences
    }

    // Remove an item from the cart
    public void removeItemFromCart(String productId) {
        HashMap<String, Integer> cart = getCart();
        if (cart.containsKey(productId)) {
            cart.remove(productId);
            saveCart(cart);  // Save updated cart back to SharedPreferences
        }
    }

    // Get the quantity of a specific item in the cart
    public int getItemQuantity(String productId) {
        HashMap<String, Integer> cart = getCart();
        Integer quantityObj = cart.get(productId);
        return (quantityObj != null) ? quantityObj : 0;
    }

    // Get the entire cart
    public HashMap<String, Integer> getCart() {
        String cartJson = sharedPreferences.getString(CART_KEY, null);
        if (cartJson == null) {
            return new HashMap<>();  // Return empty cart if not found
        }
        Type type = new TypeToken<HashMap<String, Integer>>() {}.getType();
        return gson.fromJson(cartJson, type);
    }

    // Clear the cart
    public void clearCart() {
        sharedPreferences.edit().remove(CART_KEY).apply();
    }

    // Save the cart (HashMap) to SharedPreferences
    private void saveCart(HashMap<String, Integer> cart) {
        String cartJson = gson.toJson(cart);
        sharedPreferences.edit().putString(CART_KEY, cartJson).apply();
    }
}
