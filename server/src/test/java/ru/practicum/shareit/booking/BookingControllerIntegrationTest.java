package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreationDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql("/schema.sql")
@Sql("/data.sql")
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long USER_ID_1 = 1L;
    private static final Long USER_ID_2 = 2L;
    private static final Long USER_ID_3 = 3L;
    private static final Long USER_ID_4 = 4L;

    private static final Long ITEM_ID_1 = 1L;
    private static final Long ITEM_ID_2 = 2L;

    private static final Long BOOKING_ID_1 = 1L;
    private static final Long BOOKING_ID_2 = 2L;

    private final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private final LocalDateTime end = LocalDateTime.now().plusDays(3);

    @Test
    @DisplayName("Создать бронирование. Успешно")
    void createBooking_Success() throws Exception {
        BookingCreationDto dto = new BookingCreationDto();
        dto.setItemId(ITEM_ID_2);
        dto.setStart(start.plusDays(5));
        dto.setEnd(end.plusDays(5));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", USER_ID_4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    @DisplayName("Создать бронирование. Пользователь не найден")
    void createBooking_UserNotFound() throws Exception {
        BookingCreationDto dto = new BookingCreationDto();
        dto.setItemId(ITEM_ID_1);
        dto.setStart(start);
        dto.setEnd(end);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Создать бронирование. Вещь не найдена")
    void createBooking_ItemNotFound() throws Exception {
        BookingCreationDto dto = new BookingCreationDto();
        dto.setItemId(999L);
        dto.setStart(start);
        dto.setEnd(end);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Обновить статус бронирования. Успешно")
    void updateBookingStatus_Success() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", BOOKING_ID_2)
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @DisplayName("Обновить статус бронирования. Не владелец")
    void updateBookingStatus_NotOwner() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", BOOKING_ID_2)
                        .header("X-Sharer-User-Id", USER_ID_2)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Получить бронирование по ID. Успешно")
    void getBookingById_Success() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", BOOKING_ID_1)
                        .header("X-Sharer-User-Id", USER_ID_2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOKING_ID_1))
                .andExpect(jsonPath("$.item.id").value(ITEM_ID_1));
    }

    @Test
    @DisplayName("Получить бронирование по ID. Доступ запрещён")
    void getBookingById_NoAccess() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", BOOKING_ID_1)
                        .header("X-Sharer-User-Id", USER_ID_3))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Получить список бронирований пользователя. Успешно")
    void getAllBookingsForUser_Success() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", USER_ID_2)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Получить список бронирований владельца. Успешно")
    void getAllBookingsForOwner_Success() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Создать бронирование. Конец раньше начала")
    void createBooking_EndBeforeStart() throws Exception {
        BookingCreationDto dto = new BookingCreationDto();
        dto.setItemId(ITEM_ID_1);
        dto.setStart(end);
        dto.setEnd(start);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", USER_ID_3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Обновить статус бронирования. Бронирование не найдено")
    void updateBookingStatus_BookingNotFound() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", 999L)
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .param("approved", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить список бронирований пользователя. State: PAST")
    void getAllBookingsForUser_Past() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", USER_ID_2)
                        .param("state", "PAST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Получить список бронирований пользователя. State: FUTURE")
    void getAllBookingsForUser_Future() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", USER_ID_2)
                        .param("state", "FUTURE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Обновить статус бронирования. Повторное одобрение")
    void updateBookingStatus_AlreadyApproved() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", BOOKING_ID_1)
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }
}