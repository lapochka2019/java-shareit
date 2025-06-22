package ru.practicum.shareit.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    @Test
    @DisplayName("Создание бронирования")
    void createBookingReturnResponseFromClient() {
        Long userId = 1L;
        BookingDto requestDto = new BookingDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Бронь создана");

        when(bookingClient.create(userId, requestDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingController.create(userId, requestDto);

        assertEquals(expectedResponse, actualResponse);
        verify(bookingClient, times(1)).create(userId, requestDto);
    }

    @Test
    @DisplayName("Обновление бронирования")
    void updateBookingReturnResponseFromClient() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Бронь обновлена");

        when(bookingClient.update(userId, bookingId, approved))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingController.update(userId, bookingId, approved);

        assertEquals(expectedResponse, actualResponse);
        verify(bookingClient, times(1)).update(userId, bookingId, approved);
    }

    @Test
    @DisplayName("Получение всех бронирований пользователя")
    void getBookingsReturnResponseFromClient() {
        Long userId = 1L;
        String state = "ALL";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Получить все бронирования");

        when(bookingClient.getBookings(userId, BookingState.ALL))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingController.getAllBookingForUser(userId, state);

        assertEquals(expectedResponse, actualResponse);
        verify(bookingClient, times(1)).getBookings(userId, BookingState.ALL);
    }

    @Test
    @DisplayName("Получение всех бронирований владельца")
    void getAllOwnerBookingsReturnResponseFromClient() {
        Long ownerId = 1L;
        String stateParam = "ALL";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Получить все бронирования владельца");

        when(bookingClient.getAllOwner(ownerId, BookingState.ALL))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingController.getAllBookingForOwner(ownerId, stateParam);

        assertEquals(expectedResponse, actualResponse);
        verify(bookingClient, times(1)).getAllOwner(ownerId, BookingState.ALL);
    }

    @Test
    @DisplayName("Получение информации о конкретном бронировании")
    void getBookingReturnResponseFromClient() {
        Long userId = 1L;
        Long bookingId = 2L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Информация о бронировании");

        when(bookingClient.getBooking(userId, bookingId))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingController.getBookingById(userId, bookingId);

        assertEquals(expectedResponse, actualResponse);
        verify(bookingClient, times(1)).getBooking(userId, bookingId);
    }
}
