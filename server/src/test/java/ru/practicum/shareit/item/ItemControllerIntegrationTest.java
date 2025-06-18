package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql("/schema.sql")
@Sql("/data.sql")
public class ItemControllerIntegrationTest {

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

    @Test
    @DisplayName("Создать вещь. Успешно")
    void createItem_Success() throws Exception {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Ноутбук");
        dto.setDescription("Мощный игровой ноутбук");
        dto.setAvailable(true);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", USER_ID_4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ноутбук"))
                .andExpect(jsonPath("$.description").value("Мощный игровой ноутбук"));
    }

    @Test
    @DisplayName("Создать вещь. Пользователь не найден")
    void createItem_UserNotFound() throws Exception {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Телефон");
        dto.setDescription("Смартфон Samsung Galaxy S24");
        dto.setAvailable(true);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Обновить вещь. Успешно")
    void updateItem_Success() throws Exception {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Чемодан (обновлен)");
        dto.setDescription("Большой чемодан на колесах");
        dto.setAvailable(false);

        mockMvc.perform(patch("/items/{id}", ITEM_ID_1)
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Чемодан (обновлен)"))
                .andExpect(jsonPath("$.description").value("Большой чемодан на колесах"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    @DisplayName("Обновить вещь. Не владелец")
    void updateItem_NotOwner() throws Exception {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Не мой чемодан");

        mockMvc.perform(patch("/items/{id}", ITEM_ID_1)
                        .header("X-Sharer-User-Id", USER_ID_2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить вещь по ID. Успешно")
    void getItemById_Success() throws Exception {
        mockMvc.perform(get("/items/{id}", ITEM_ID_1)
                        .header("X-Sharer-User-Id", USER_ID_2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID_1))
                .andExpect(jsonPath("$.name").value("Чемодан"))
                .andExpect(jsonPath("$.description").value("Большой чемодан на 4 колесах"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner.id").value(USER_ID_1));
    }

    @Test
    @DisplayName("Получить вещь по ID. Пользователь не существует")
    void getItemById_UserNotFound() throws Exception {
        mockMvc.perform(get("/items/{id}", ITEM_ID_1)
                        .header("X-Sharer-User-Id", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить список вещей владельца. Успешно")
    void getItemsForOwner_Success() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(ITEM_ID_1));
    }

    @Test
    @DisplayName("Поиск вещей. Успешно")
    void searchItems_Success() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "велосипед"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Велосипед"));
    }

    @Test
    @DisplayName("Поиск вещей. Пустой запрос")
    void searchItems_EmptyText() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Добавить комментарий к вещи. Успешно")
    void addCommentToItem_Success() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Отличная вещь!");

        mockMvc.perform(post("/items/{itemId}/comment", ITEM_ID_1)
                        .header("X-Sharer-User-Id", USER_ID_2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Отличная вещь!"))
                .andExpect(jsonPath("$.authorName").value("Мария Петрова"));
    }

    @Test
    @DisplayName("Добавить комментарий к вещи. Пользователь не брал в аренду")
    void addCommentToItem_UserNotRented() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Хочу оставить отзыв");

        mockMvc.perform(post("/items/{itemId}/comment", ITEM_ID_1)
                        .header("X-Sharer-User-Id", USER_ID_3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Обновить вещь. Некорректный ID")
    void updateItem_InvalidId() throws Exception {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Новое имя");

        mockMvc.perform(patch("/items/{id}", -1L)
                        .header("X-Sharer-User-Id", USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить вещь по ID. Вещь не найдена")
    void getItemById_ItemNotFound() throws Exception {
        mockMvc.perform(get("/items/{id}", 999L)
                        .header("X-Sharer-User-Id", USER_ID_1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Добавить комментарий к вещи. Вещь не существует")
    void addCommentToItem_ItemNotFound() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Хочу оставить отзыв");

        mockMvc.perform(post("/items/{itemId}/comment", 999L)
                        .header("X-Sharer-User-Id", USER_ID_4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Поиск вещей. Регистр не важен")
    void searchItems_CaseInsensitive() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "ВЕЛОСИПЕД"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Поиск вещей. Пробелы игнорируются")
    void searchItems_WhitespaceIgnored() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", " велосипед "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Создать вещь. Несуществующий requestId")
    void createItem_InvalidRequestId() throws Exception {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Ноутбук");
        dto.setDescription("Мощный игровой ноутбук");
        dto.setAvailable(true);
        dto.setRequestId(999L); // requestId не существует

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", USER_ID_4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}