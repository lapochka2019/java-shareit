package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static ru.practicum.shareit.utils.Constants.USER_ID_HEADER;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) Long user,
                                         @Valid @RequestBody BookingDto bookingDto) {
        log.info("Запрос на бронирование {} от пользователя {}", bookingDto, user);
        return bookingClient.create(user, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(USER_ID_HEADER) Long user,
                                         @PathVariable("bookingId") Long bookingId,
                                         @RequestParam Boolean approved) {
        log.info("{} бронирование {}", approved ? "Одобрить" : "Отклонить", bookingId);
        return bookingClient.update(user, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID_HEADER) Long user,
                                                 @PathVariable("bookingId") Long bookingId) {
        log.info("Пользователь {} запрашивает информацию о бронировании {}", user, bookingId);
        return bookingClient.getBooking(user, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllBookingForUser(@RequestHeader(USER_ID_HEADER) Long user,
                                                       @RequestParam(value = "state", defaultValue = "ALL") String state) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Пользователь {} запрашивает информацию о {} бронированиях", user, state);
        return bookingClient.getBookings(user, bookingState);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingForOwner(@RequestHeader(USER_ID_HEADER) Long user,
                                                        @RequestParam(value = "state", defaultValue = "ALL") String state) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Владелец вещей {} запрашивает информацию о {} бронированиях", user, state);
        return bookingClient.getAllOwner(user, bookingState);
    }
}
