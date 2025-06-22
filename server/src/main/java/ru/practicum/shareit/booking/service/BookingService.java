package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(Long userId, BookingCreationDto bookingCreationDto);

    BookingDto bookingApproved(Long userId, Long bookingId, boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getBookingByUserId(Long id, String state);

    List<BookingDto> getBookingByOwnerId(Long id, String state);
}
