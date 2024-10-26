package com.example.milk_store_app.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateRequest {
    private int orderStatus;
    private String address;
    private String phoneNumber;
}
