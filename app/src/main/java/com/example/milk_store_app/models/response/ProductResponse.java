package com.example.milk_store_app.models.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
    private boolean isDeleted;
    private String brandName;


}
