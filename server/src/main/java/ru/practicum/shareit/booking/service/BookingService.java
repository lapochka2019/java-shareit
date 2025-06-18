package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(Long userId, BookingDto bookingDto);

    Booking bookingApproved(Long userId, Long bookingId, boolean approved);

    Booking getBooking(Long userId, Long bookingId);

    List<Booking> getBookingByUserId(Long id, String state);

    List<Booking> getBookingByOwnerId(Long id, String state);
}
