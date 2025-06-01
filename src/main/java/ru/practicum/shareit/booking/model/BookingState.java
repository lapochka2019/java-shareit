package ru.practicum.shareit.booking.model;

import java.util.Arrays;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState from(String bookingState) {
        return Arrays.stream(BookingState.values())
                .filter(value -> value.name().equalsIgnoreCase(bookingState))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неверно указан параметр state: " + bookingState));
    }
}