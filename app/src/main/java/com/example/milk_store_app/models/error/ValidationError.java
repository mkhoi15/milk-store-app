package com.example.milk_store_app.models.error;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationError {
    private Map<String, List<String>> errors;
    private String type;
    private String title;
    private int status;
    private String traceId;
}
