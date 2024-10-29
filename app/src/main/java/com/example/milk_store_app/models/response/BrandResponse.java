package com.example.milk_store_app.models.response;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class BrandResponse {
    private String id;
    private String name;

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
