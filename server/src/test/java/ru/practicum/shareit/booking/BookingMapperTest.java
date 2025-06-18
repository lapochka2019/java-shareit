package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    private final BookingMapper mapper = BookingMapper.INSTANCE;

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime LATER = LocalDateTime.now().plusDays(1);

    @Test
    void toBooking_shouldMapCorrectly() {
        Long id = 1L;
        BookingCreationDto dto = new BookingCreationDto();
        dto.setItemId(100L);
        dto.setStart(NOW);
        dto.setEnd(LATER);

        Item item = new Item();
        item.setId(100L);

        User user = new User();
        user.setId(200L);

        BookingStatus status = BookingStatus.WAITING;

        Booking booking = mapper.toBooking(id, dto, item, user, status);

        assertThat(booking).isNotNull();
        assertThat(booking.getId()).isEqualTo(id);
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(user);
        assertThat(booking.getStatus()).isEqualTo(status);
        assertThat(booking.getStart()).isEqualTo(NOW);
        assertThat(booking.getEnd()).isEqualTo(LATER);
    }

    @Test
    void toBookingCreationDto_shouldMapCorrectly() {
        Item item = new Item();
        item.setId(100L);

        User user = new User();
        user.setId(200L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(NOW);
        booking.setEnd(LATER);

        BookingCreationDto dto = mapper.toBookingCreationDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getItemId()).isEqualTo(100L);
        assertThat(dto.getStart()).isEqualTo(NOW);
        assertThat(dto.getEnd()).isEqualTo(LATER);
    }

    @Test
    void toBookingDto_shouldMapCorrectly() {
        Item item = new Item(1L, "Item", "Description", true, 1L, null);

        User user = new User(2L, "User", "user@mail.ru");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(NOW);
        booking.setEnd(LATER);

        BookingDto dto = mapper.toBookingDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(NOW);
        assertThat(dto.getEnd()).isEqualTo(LATER);
        assertThat(dto.getItem()).isEqualTo(ItemMapper.INSTANCE.toItemDtoForRequest(item));
        assertThat(dto.getBooker()).isEqualTo(UserMapper.INSTANCE.toDto(user));
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}