package com.example.milk_store_app.session;

import android.content.Context;

import com.example.milk_store_app.database.CartDatabase;
import com.example.milk_store_app.database.dao.CartItemDao;
import com.example.milk_store_app.models.entities.CartItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CartManager {
    private final CartItemDao cartItemDao;
    private final ExecutorService executorService;

    public CartManager(Context context) {
        CartDatabase db = CartDatabase.getInstance(context);
        cartItemDao = db.cartItemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Add or update an item in the cart
    public void addItemToCart(String productId, String productName, double productPrice, int quantity) {
        executorService.execute(() -> {
            CartItems item = cartItemDao.getItemByProductId(productId);
            if (item == null) {
                // Add new item if not found in the cart
                cartItemDao.insertItem(new CartItems(0, productId, productName, productPrice, quantity));
            } else {
                // Update existing item quantity if already in the cart
                item.setQuantity(item.getQuantity() + quantity);
                cartItemDao.updateItem(item);
            }
        });
    }

    // Remove an item from the cart
    public void removeItemFromCart(String productId) {
        executorService.execute(() -> {
            CartItems item = cartItemDao.getItemByProductId(productId);
            if (item != null) {
                cartItemDao.deleteItem(item);
            }
        });
    }

    // Get the quantity of a specific item in the cart
    public int getItemQuantity(String productId) {
        CartItems item = cartItemDao.getItemByProductId(productId);
        return (item != null) ? item.getQuantity() : 0;
    }

    //Set the quantity of a specific item in the cart
    public void setItemQuantity(String productId, int quantity) {
        CartItems item = cartItemDao.getItemByProductId(productId);
        if (item != null) item.setQuantity(quantity);
    }

    // Get the entire cart
    public List<CartItems> getCart() {
        List<CartItems> cartItems = new ArrayList<>();
        Future<?> future = executorService.submit(() -> {
            cartItems.addAll(cartItemDao.getAllItems());
        });
        try {
            future.get();
        } catch (Exception e) {
            return cartItems;
        }
        return cartItems;
    }

    public double getTotalPrice() {
        List<CartItems> cartItems = getCart();

        return cartItems.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();
    }

    // Clear the cart
    public void clearCart() {
        executorService.execute(cartItemDao::clearCart);
    }
}
