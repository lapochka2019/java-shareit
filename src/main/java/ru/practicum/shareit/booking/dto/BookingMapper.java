package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }

    public static Booking toBooking(Long id, BookingDto dto, Item item, User user, BookingStatus status) {
        return new Booking(
                id,
                dto.getStart(),
                dto.getEnd(),
                item,
                user,
                status
        );
    }
}
