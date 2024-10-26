package com.example.milk_store_app.models.response;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderDeliveryResponse implements Serializable {
    private String customerName;
    private String address;
    private String phoneNumber;
    private String orderCode;
    private String orderDate;
    private BigDecimal totalPrice;
    private String orderStatus;
}
