package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.USER_ID_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    @PostMapping
    public BookingDto create(@RequestHeader(USER_ID_HEADER) Long user,
                             @RequestBody BookingCreationDto bookingCreationDto) {
        log.info("Запрос на бронирование {} от пользователя {}", bookingCreationDto, user);
        return service.createBooking(user, bookingCreationDto);
    }

    @PatchMapping("/{bookingId}")//?approved={approved}
    public BookingDto update(@RequestHeader(USER_ID_HEADER) Long user,
                             @PathVariable("bookingId") Long bookingId,
                             @RequestParam Boolean approved) {
        log.info("{} бронирование {}", approved ? "Одобрить" : "Отклонить", bookingId);
        return service.bookingApproved(user, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(USER_ID_HEADER) Long user,
                                     @PathVariable("bookingId") Long bookingId) {
        log.info("Пользователь {} запрашивает информацию о бронировании {}", user, bookingId);
        return service.getBooking(user, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingForUser(@RequestHeader(USER_ID_HEADER) Long user,
                                                 @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Пользователь {} запрашивает информацию о {} бронированиях", user, state);
        return service.getBookingByUserId(user, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingForOwner(@RequestHeader(USER_ID_HEADER) Long user,
                                                  @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Владелец вещей {} запрашивает информацию о {} бронированиях", user, state);
        return service.getBookingByOwnerId(user, state);
    }
}