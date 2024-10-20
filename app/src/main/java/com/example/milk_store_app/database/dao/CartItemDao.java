package com.example.milk_store_app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.milk_store_app.models.entities.CartItems;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert
    void insertItem(CartItems item);

    @Update
    void updateItem(CartItems item);

    @Delete
    void deleteItem(CartItems item);

    @Query("SELECT * FROM cart_items")
    List<CartItems> getAllItems();

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    CartItems getItemByProductId(String productId);

    @Query("DELETE FROM cart_items")
    void clearCart();
}
