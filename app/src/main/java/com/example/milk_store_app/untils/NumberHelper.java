package com.example.milk_store_app.untils;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberHelper {
    /**
     * Format a number to currency format
     * <p>
     *     The currency format is based on the Vietnamese locale
     *     and the currency symbol is "₫" at the end of the number
     * </p>
     * @param number the number to format
     * @return the formatted number as a string
     */
    @NonNull
    public static String formatNumber(BigDecimal number) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));  // Using Vietnamese currency
        return currencyFormat.format(number);
    }
}
