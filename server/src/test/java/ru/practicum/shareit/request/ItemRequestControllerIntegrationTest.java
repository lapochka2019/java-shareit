package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql("/schema.sql")
@Sql("/data.sql")
public class ItemRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long USER_ID_1 = 1L;
    private static final Long USER_ID_2 = 2L;
    private static final Long USER_ID_3 = 3L;
    private static final Long USER_ID_4 = 4L;

    private static final Long REQUEST_ID_1 = 1L;

    private static final String DESCRIPTION = "Хочу найти чемодан на неделю";

    @Test
    @DisplayName("Создать запрос. Успешно")
    void createRequest_Success() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Нужен ноутбук");

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", USER_ID_3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description").value("Нужен ноутбук"))
                .andExpect(jsonPath("$.requesterId").value(3));
    }

    @Test
    @DisplayName("Создать запрос. Пользователь не найден")
    void createRequest_UserNotFound() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Неизвестный пользователь");

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить запросы пользователя. Успешно")
    void getUserRequests_Success() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", USER_ID_2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(REQUEST_ID_1))
                .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
                .andExpect(jsonPath("$[0].items").isArray())
                .andExpect(jsonPath("$[0].items.length()").value(1));
    }

    @Test
    @DisplayName("Получить запросы пользователя. Пользователь не найден")
    void getUserRequests_UserNotFound() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить все запросы. Успешно")
    void getAllRequests_Success() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Получить запрос по ID. Успешно")
    void getRequestById_Success() throws Exception {
        mockMvc.perform(get("/requests/{id}", REQUEST_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(REQUEST_ID_1))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    @DisplayName("Получить запрос по ID. Запрос не найден")
    void getRequestById_RequestNotFound() throws Exception {
        mockMvc.perform(get("/requests/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить все запросы. Пагинация: offset=0, limit=1")
    void getAllRequests_Pagination_Offset0_Limit1() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .param("offset", "0")
                        .param("limit", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Получить все запросы. Пагинация: offset больше количества")
    void getAllRequests_Pagination_OffsetTooBig() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .param("offset", "10")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Получить запрос по ID. Некорректный ID (отрицательный)")
    void getRequestById_InvalidId_Negative() throws Exception {
        mockMvc.perform(get("/requests/{id}", -1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить запрос по ID. Нулевой ID")
    void getRequestById_InvalidId_Zero() throws Exception {
        mockMvc.perform(get("/requests/{id}", 0L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить запрос по ID. Нет связанных вещей")
    void getRequestById_NoItems() throws Exception {
        mockMvc.perform(get("/requests/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    @DisplayName("Получить запросы пользователя. У пользователя нет запросов")
    void getUserRequests_UserWithoutRequests() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", USER_ID_3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}