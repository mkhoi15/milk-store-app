package com.example.milk_store_app.models.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.milk_store_app.constants.RoomDataBaseConstants;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = RoomDataBaseConstants.TABLE_NAME)
public class CartItems {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private int quantity;
}
