package ru.practicum.shareit.booking.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    @PostMapping
    public Booking create(@RequestHeader("X-Sharer-User-Id") Long user,
                          @Valid @RequestBody BookingDto bookingDto) {
        log.info("Запрос на бронирование {} от пользователя {}", bookingDto, user);
        return service.createBooking(user, bookingDto);
    }

    @PatchMapping("/{bookingId}")//?approved={approved}
    public Booking update(@RequestHeader("X-Sharer-User-Id") Long user,
                          @PathVariable("bookingId") Long bookingId,
                          @RequestParam Boolean approved) {
        log.info("{} бронирование {}", approved ? "Одобрить" : "Отклонить", bookingId);
        return service.bookingApproved(user, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Long user,
                                  @PathVariable("bookingId") Long bookingId) {
        log.info("Пользователь {} запрашивает информацию о бронировании {}", user, bookingId);
        return service.getBooking(user, bookingId);
    }

    @GetMapping()
    public List<Booking> getAllBookingForUser(@RequestHeader("X-Sharer-User-Id") Long user,
                                              @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Пользователь {} запрашивает информацию о {} бронированиях", user, state);
        return service.getBookingByUserId(user, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingForOwner(@RequestHeader("X-Sharer-User-Id") Long user,
                                               @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Владелец вещей {} запрашивает информацию о {} бронированиях", user, state);
        return service.getBookingByOwnerId(user, state);
    }
}
