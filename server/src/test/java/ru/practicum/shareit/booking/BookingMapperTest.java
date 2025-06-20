package ru.practicum.shareit.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    @Test
    @DisplayName("Преобразование DTO в Booking со всеми заполненными полями")
    void testToBooking_withAllFieldsFilled() {
        LocalDateTime now = LocalDateTime.now();
        Item item = new Item(1L, "ItemName", "desc", true, null, null);
        User user = new User(1L, "user@example.com", "User");
        BookingCreationDto dto = new BookingCreationDto(1L, now, now.plusHours(1));
        Long id = 100L;
        BookingStatus status = BookingStatus.APPROVED;

        Booking booking = BookingMapper.INSTANCE.toBooking(id, dto, item, user, status);

        assertThat(booking).isNotNull();
        assertThat(booking.getId()).isEqualTo(id);
        assertThat(booking.getStart()).isEqualTo(dto.getStart());
        assertThat(booking.getEnd()).isEqualTo(dto.getEnd());
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(user);
        assertThat(booking.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Преобразование Booking в BookingCreationDto с заполненными данными")
    void testToBookingCreationDto_withValidBooking() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 2, 10, 0);
        Item item = new Item(5L, "ItemName", "desc", true, null, null);
        Booking booking = new Booking(1L, start, end, item, new User(), BookingStatus.WAITING);

        BookingCreationDto dto = BookingMapper.INSTANCE.toBookingCreationDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getItemId()).isEqualTo(5L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
    }

    @Test
    @DisplayName("Преобразование Booking в BookingDto с заполненными данными")
    void testToBookingDto_withValidBooking() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 2, 10, 0);
        Item item = new Item(5L, "ItemName", "desc", true, null, null);
        User booker = new User(10L, "Name", "email@example.com");
        Booking booking = new Booking(1L, start, end, item, booker, BookingStatus.REJECTED);

        BookingDto dto = BookingMapper.INSTANCE.toBookingDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
        assertThat(dto.getItem().getId()).isEqualTo(5L);
        assertThat(dto.getItem().getName()).isEqualTo("ItemName");
        assertThat(dto.getBooker().getId()).isEqualTo(10L);
        assertThat(dto.getBooker().getEmail()).isEqualTo("email@example.com");
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    @Test
    @DisplayName("Преобразование Booking в BookingDto с null ссылками")
    void testToBookingDto_withNullReferences() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.CANCELED);

        BookingDto dto = BookingMapper.INSTANCE.toBookingDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getItem()).isNull();
        assertThat(dto.getBooker()).isNull();
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.CANCELED);
    }

    @Test
    @DisplayName("Конвертация Booking в BookingCreationDto с заполненными данными")
    void testToBookingCreationDto_withValidData() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 2, 10, 0);
        Item item = new Item(5L, "ItemName", "desc", true, null, null);
        Booking booking = new Booking(1L, start, end, item, new User(), BookingStatus.WAITING);

        BookingCreationDto dto = BookingMapper.INSTANCE.toBookingCreationDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getItemId()).isEqualTo(5L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
    }

    @Test
    @DisplayName("Конвертация Booking с null значением item")
    void testToBookingCreationDto_withNullItem() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));

        BookingCreationDto dto = BookingMapper.INSTANCE.toBookingCreationDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getItemId()).isNull();
        assertThat(dto.getStart()).isEqualTo(booking.getStart());
        assertThat(dto.getEnd()).isEqualTo(booking.getEnd());
    }

    @Test
    @DisplayName("Конвертация Booking с пустым объектом item")
    void testToBookingCreationDto_withEmptyItem() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.MIN);
        booking.setEnd(LocalDateTime.MAX);

        BookingCreationDto dto = BookingMapper.INSTANCE.toBookingCreationDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getItemId()).isNull();
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.MIN);
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.MAX);
    }

    @Test
    @DisplayName("Конвертация Booking с null датами")
    void testToBookingCreationDto_withNullDates() {
        Item item = new Item(1L, "TestItem", "description", true, null, null);
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(null);
        booking.setEnd(null);

        BookingCreationDto dto = BookingMapper.INSTANCE.toBookingCreationDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isNull();
        assertThat(dto.getEnd()).isNull();
    }

    @Test
    @DisplayName("Конвертация null в CreationDto")
    void testToBookingCreationDto_withNullBooking() {
        BookingCreationDto dto = BookingMapper.INSTANCE.toBookingCreationDto(null);

        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Конвертация null в BookingDto")
    void testToBookingDto_withNullBooking() {
        BookingDto dto = BookingMapper.INSTANCE.toBookingDto(null);

        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Конвертация null в Booking")
    void testToBooking_withNullBooking() {
        Booking booking = BookingMapper.INSTANCE.toBooking(null, null, null, null, null);

        assertThat(booking).isNull();
    }
}