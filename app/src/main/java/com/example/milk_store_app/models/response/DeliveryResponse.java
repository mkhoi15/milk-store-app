package com.example.milk_store_app.models.response;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponse implements Serializable {
    private UUID id;
    private UUID orderId;
    private UUID deliveryStaffId;
    private String deliveryDate;
    private String deliveryStaffName;
    private OrderDeliveryResponse order;
}

