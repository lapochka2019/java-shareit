package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;


    @Override
    public Booking createBooking(Long userId, BookingDto bookingDto) {
        log.info("Проверка корректной даты бронирования {}", bookingDto);
        checkData(bookingDto);
        log.info("Получение вещи, которую хотят забронировать");
        Item item = itemService.checkItemExist(bookingDto.getItemId());
        if (!item.getAvailable()) {
            log.error("Вещь недоступна для бронирования.");
            throw new IllegalStateException("Вещь недоступна для бронирования.");
        }
        log.info("Получение пользователя, который хочет забронировать вещь");
        User user = UserMapper.INSTANCE.toEntity(userService.getUser(userId));

        Booking booking = bookingMapper.toBooking(0L, bookingDto, item, user, BookingStatus.WAITING);
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
        BookingState state = parseState(stateString);
        log.info("Получение пользователя");
        userService.getUser(id);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(id);
            case CURRENT -> bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(id, now, now);
            case PAST -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(id, now);
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(id, now);
            case WAITING ->
                    bookingRepository.findByBookerIdAndStatusAndStartAfterOrderByStartDesc(id, BookingStatus.WAITING, now);
            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(id, BookingStatus.REJECTED);
        };
        return bookings;
    }

    @Override
    public List<Booking> getBookingByOwnerId(Long id, String stateString) {
        log.info("Проверяем параметр stateString");
        BookingState state = parseState(stateString);
        log.info("Получение пользователя (владельца)");
        userService.getUser(id);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByItem_OwnerOrderByStartDesc(id);
            case CURRENT -> bookingRepository.findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(id, now, now);
            case PAST -> bookingRepository.findByItem_OwnerAndEndBeforeOrderByStartDesc(id, now);
            case FUTURE -> bookingRepository.findByItem_OwnerAndStartAfterOrderByStartDesc(id, now);
            case WAITING ->
                    bookingRepository.findByItem_OwnerAndStatusAndStartAfterOrderByStartDesc(id, BookingStatus.WAITING, now);
            case REJECTED -> bookingRepository.findByItem_OwnerAndStatusOrderByStartDesc(id, BookingStatus.REJECTED);
        };
        return bookings;
    }

    public void checkData(BookingDto dto) {
        if (!dto.getStart().isBefore(dto.getEnd())) {
            throw new IllegalArgumentException("Время начала бронирования должно быть раньше конца бронирования");
        }
    }

    public static BookingState parseState(String stateString) {
        try {
            return BookingState.valueOf(stateString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неизвестный BookingState: " + stateString);
        }
    }

}
