package com.example.milk_store_app.untils;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberHelper {
    @NonNull
    public static String formatNumber(BigDecimal number) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));  // Using Vietnamese currency
        return currencyFormat.format(number);
    }
}
