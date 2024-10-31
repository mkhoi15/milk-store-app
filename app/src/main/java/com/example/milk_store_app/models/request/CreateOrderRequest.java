package com.example.milk_store_app.models.request;

import java.util.UUID;

public class CreateOrderRequest {

    private UUID orderId;
    private UUID deliveryStaffId;

    public CreateOrderRequest(UUID orderId, UUID deliveryStaffId) {
        this.orderId = orderId;
        this.deliveryStaffId = deliveryStaffId;
    }
}
