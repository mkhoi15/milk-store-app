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
public class UserReponse {
    private UUID userId;
    private String username;
    private String fullName;
    private String address;
    private String email;
    private String phoneNumber;
}
