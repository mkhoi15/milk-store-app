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
public class OrderResponse {
    private UUID id;
    private UUID userId;
    private String orderCode;
    private String orderDate;
    private String orderStatus;
    private BigDecimal totalPrice;
    private String address;
    private String phoneNumber;
}
