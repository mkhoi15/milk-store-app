package com.example.milk_store_app.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PutOrderRequest {
    private int orderStatus;
    private String orderCode;
    private String address;
    private String phoneNumber;
}
