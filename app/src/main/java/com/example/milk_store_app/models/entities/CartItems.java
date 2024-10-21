package com.example.milk_store_app.models.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.milk_store_app.constants.RoomDataBaseConstants;

@Entity(tableName = RoomDataBaseConstants.TABLE_NAME)
public class CartItems {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String productId;
    private String productName;
    private double productPrice;
    private int quantity;

    public CartItems(int id, String productId, String productName, double productPrice, int quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
