package com.example.milk_store_app.models.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemListResponse{
    private int quantity;
    private BigDecimal price;
    private String productName;

    @Override
    public String toString() {
        return productName + " - " + quantity + " x " + price;
    }
}
