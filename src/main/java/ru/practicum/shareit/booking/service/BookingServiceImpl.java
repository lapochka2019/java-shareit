package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    ItemService itemService;
    UserService userService;
    BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(ItemService itemService, UserService userService, BookingRepository bookingRepository) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createBooking(Long userId, BookingDto bookingDto) {
        log.info("Проверка корректной даты бронирования {}", bookingDto);
        checkData(bookingDto);
        log.info("Получение вещи, которую хотят забронировать");
        Item item = itemService.getItem(bookingDto.getItemId());
        if (!item.getAvailable()) {
            log.error("Вещь недоступна для бронирования.");
            throw new IllegalStateException("Вещь недоступна для бронирования.");
        }
        log.info("Получение пользователя, который хочет забронировать вещь");
        User user = userService.getUser(userId);

        Booking booking = BookingMapper.toBooking(0L, bookingDto, item, user, BookingStatus.WAITING);
        log.info("Сохраняем запрос на бронирование");
        return bookingRepository.save(booking);
    }

    @Override
    public Booking bookingApproved(Long userId, Long bookingId, boolean approved) {
        log.info("Получаем информацию о запросе бронирования {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));

        log.info("Проверяем статус бронирования");
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalStateException("Статус можно изменить только у бронирования со статусом WAITING.");
        }

        log.info("Получаем вещь, которую хотят забронировать");
        Item item = booking.getItem();

        if (userId.equals(item.getOwner())) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            log.info("Изменение статуса бронирования");
            return bookingRepository.save(booking);
        } else {
            throw new ValidationException("Одобрить бронирование может только владелец вещи");
        }
    }

    @Override
    public Booking getBooking(Long userId, Long bookingId) {
        log.info("Получаем информацию о запросе бронирования {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        log.info("Получение вещи, которую хотят забронировать");
        Item item = booking.getItem();
        if (userId.equals(item.getOwner()) || userId.equals(booking.getBooker().getId())) {
            return booking;
        } else {
            throw new IllegalArgumentException("Получить информацию о бронировании может только владелец вещи или бронирующий");
        }
    }

    @Override
    public List<Booking> getBookingByUserId(Long id, String stateString) {
        log.info("Проверяем параметр stateString");
        BookingState state = BookingState.from(stateString);
        log.info("Получение пользователя");
        User user = userService.getUser(id);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllBookingByBookerId(id);
            case CURRENT -> bookingRepository.findCurrentBookingByBookerId(id, now);
            case PAST -> bookingRepository.findPastBookingByBookerId(id, now);
            case FUTURE -> bookingRepository.findFutureBookingByBookerId(id, now);
            case WAITING -> bookingRepository.findWaitingBookingByBookerId(id, now);
            case REJECTED -> bookingRepository.findRejectBookingByBookerId(id);
        };
        return bookings;
    }

    @Override
    public List<Booking> getBookingByOwnerId(Long id, String stateString) {
        log.info("Проверяем параметр stateString");
        BookingState state = BookingState.from(stateString);
        log.info("Получение пользователя (владельца)");
        User user = userService.getUser(id);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllBookingsByOwnerId(id);
            case CURRENT -> bookingRepository.findAllCurrentBookingsByOwnerId(id, now);
            case PAST -> bookingRepository.findAllPastBookingsByOwnerId(id, now);
            case FUTURE -> bookingRepository.findAllFutureBookingsByOwnerId(id, now);
            case WAITING -> bookingRepository.findAllWaitingBookingsByOwnerId(id, now);
            case REJECTED -> bookingRepository.findAllRejectedBookingsByOwnerId(id);
        };
        return bookings;
    }

    public void checkData(BookingDto dto) {
        if (!dto.getStart().isBefore(dto.getEnd())) {
            throw new IllegalArgumentException("Время начала бронирования должно быть раньше конца бронирования");
        }
    }

}
