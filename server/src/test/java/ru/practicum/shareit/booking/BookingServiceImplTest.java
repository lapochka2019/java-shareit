package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private LocalDateTime now;
    private ItemDto item;
    private Long userId;
    private Long ownerId;
    private Long itemId;
    private BookingCreationDto dto;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now().plusDays(1);
        UserDto user = userService.create(new UserDto(0L, "UserName", "user123@mail.ru"));
        userId = user.getId();
        UserDto user1 = userService.create(new UserDto(0L, "UserName", "user1234@mail.ru"));
        ownerId = user1.getId();

        item = itemService.create(new ItemCreateDto("ItemName", "Description", true, null), ownerId);
        itemId = item.getId();

        dto = new BookingCreationDto();
        dto.setItemId(itemId);
        dto.setStart(now.plusHours(1));
        dto.setEnd(now.plusHours(2));
    }

    @Test
    @DisplayName("Создание бронирования: успешно")
    void createBooking_whenValid_shouldCreate() {
        BookingDto saved = bookingService.createBooking(userId, dto);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getItem().getId()).isEqualTo(itemId);
        assertThat(saved.getBooker().getId()).isEqualTo(userId);
        assertThat(saved.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    @DisplayName("Создание бронирования: вещь недоступна")
    void createBooking_whenItemNotAvailable_shouldThrowException() {
        itemService.update(new ItemCreateDto("ItemName", "Description", false, null), ownerId, itemId);
        assertThatThrownBy(() -> bookingService.createBooking(userId, dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Вещь недоступна для бронирования.");
    }

    @Test
    @DisplayName("Создание бронирования: пользователь не существует")
    void createBooking_whenUserNotFound_shouldThrowException() {
        assertThatThrownBy(() -> bookingService.createBooking(99L, dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Создание бронирования: дата начала после окончания")
    void createBooking_whenStartDateAfterEndDate_shouldThrowException() {
        BookingCreationDto invalidDto = new BookingCreationDto();
        invalidDto.setItemId(1L);
        invalidDto.setStart(LocalDateTime.of(2025, 1, 1, 12, 0));
        invalidDto.setEnd(LocalDateTime.of(2025, 1, 1, 11, 0));

        assertThatThrownBy(() -> bookingService.createBooking(userId, invalidDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Время начала бронирования должно быть раньше конца бронирования");
    }

    @Test
    @DisplayName("Одобрение бронирования: успешно")
    void bookingApproved_whenOwnerAndWaiting_shouldApprove() {
        BookingDto created = bookingService.createBooking(userId, dto);

        BookingDto updated = bookingService.bookingApproved(ownerId, created.getId(), true);

        assertThat(updated.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Одобрение бронирования: не владелец")
    void bookingApproved_whenNotOwner_shouldThrowException() {
        BookingDto created = bookingService.createBooking(userId, dto);

        assertThatThrownBy(() -> bookingService.bookingApproved(99L, created.getId(), true))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Одобрить бронирование может только владелец вещи");
    }

    @Test
    @DisplayName("Получение бронирования по ID: пользователь не участник")
    void getBooking_whenUserNotParticipant_shouldThrowException() {
        BookingDto created = bookingService.createBooking(userId, dto);

        assertThatThrownBy(() -> bookingService.getBooking(1L, created.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Получить информацию о бронировании может только владелец вещи или бронирующий");
    }

    @Test
    @DisplayName("Получение бронирования по ID: успешно")
    void getBooking_whenUserIsParticipant_shouldReturnBooking() {
        BookingDto created = bookingService.createBooking(userId, dto);
        BookingDto booking = bookingService.getBooking(userId, created.getId());

        assertThat(booking).isNotNull();
        assertThat(booking.getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("Получение бронирований пользователя: ALL")
    void getBookingByUserId_withAllState_shouldReturnList() {
        BookingDto created = bookingService.createBooking(userId, dto);
        List<BookingDto> bookings = bookingService.getBookingByUserId(userId, "ALL");

        assertThat(bookings).isNotEmpty();
    }

    @Test
    @DisplayName("Получение бронирований пользователя: UNKNOWN state")
    void getBookingByUserId_withUnknownState_shouldThrowException() {
        String invalidState = "UNKNOWN";

        assertThatThrownBy(() -> bookingService.getBookingByUserId(userId, invalidState))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Неизвестный BookingState: " + invalidState);
    }

    @Test
    @DisplayName("Получение бронирований владельца: FUTURE")
    void getBookingByOwnerId_withFutureState_shouldReturnList() {
        BookingDto created = bookingService.createBooking(userId, dto);
        BookingDto updated = bookingService.bookingApproved(ownerId, created.getId(), true);
        List<BookingDto> bookings = bookingService.getBookingByOwnerId(ownerId, "FUTURE");

        assertThat(bookings).isNotEmpty();
    }
}