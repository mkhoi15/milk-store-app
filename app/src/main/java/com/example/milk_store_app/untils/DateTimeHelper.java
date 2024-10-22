package com.example.milk_store_app.untils;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {
    /**
     * Parse a string to LocalDateTime
     * @param dateString the string to parse
     * @return the LocalDateTime object
     */
    public static LocalDateTime parseStringToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(dateString, formatter);
    }

    /**
     * Format a LocalDateTime object to a string with the format "yyyy-MM-dd HH:mm:ss"
     * <p>
     *     example: "2021-09-01 12:00:00"
     * </p>
     * @param localDateTime the LocalDateTime object to format
     * @return the formatted string
     */
    public static String formatLocalDateTimeToString(@NonNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
