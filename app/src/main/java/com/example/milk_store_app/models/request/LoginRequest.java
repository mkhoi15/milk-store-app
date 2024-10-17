package com.example.milk_store_app.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class LoginRequest {
    private String username;
    private String password;
}
