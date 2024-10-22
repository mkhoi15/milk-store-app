package com.example.milk_store_app.models.request;

import com.example.milk_store_app.models.entities.OrderItem;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostOrderRequest {
    private String userId;
    private String orderCode;
    private double totalPrice;
    private String address;
    private String phoneNumber;
    private List<OrderItem> orderDetails;
}
