package com.example.milk_store_app.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private int role;
}
