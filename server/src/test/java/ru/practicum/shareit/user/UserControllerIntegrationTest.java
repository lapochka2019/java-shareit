package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql("/schema.sql")
@Sql("/data.sql")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long USER_ID_1 = 1L;
    private static final Long USER_ID_2 = 2L;
    private static final Long USER_ID_3 = 3L;
    private static final Long USER_ID_4 = 4L;
    private static final Long INVALID_USER_ID = 999L;

    @Test
    @DisplayName("Создание пользователя. Успешно")
    void createUser_Success() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Новый пользователь");
        dto.setEmail("newuser@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Новый пользователь"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }

    @Test
    @DisplayName("Создание пользователя. Email уже существует")
    void createUser_EmailAlreadyExists() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Мария Петрова");
        dto.setEmail("petrova@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Обновить пользователя. Успешно")
    void updateUser_Success() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Обновлённое имя");
        dto.setEmail("updated@example.com");

        mockMvc.perform(patch("/users/{id}", USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Обновлённое имя"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    @DisplayName("Обновить пользователя. Не существует")
    void updateUser_UserNotFound() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Неизвестный пользователь");

        mockMvc.perform(patch("/users/{id}", INVALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Обновить пользователя. Дублирующийся email")
    void updateUser_DuplicateEmail() throws Exception {
        UserDto dto = new UserDto();
        dto.setEmail("ivanov@example.com"); // такой email уже есть у USER_ID_1

        mockMvc.perform(patch("/users/{id}", USER_ID_2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Обновить пользователя. Нулевой ID")
    void updateUser_NullId() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Попытка обновления без ID");

        mockMvc.perform(patch("/users/{id}", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить пользователя по ID. Успешно")
    void getUserById_Success() throws Exception {
        mockMvc.perform(get("/users/{id}", USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID_1))
                .andExpect(jsonPath("$.name").value("Алексей Иванов"))
                .andExpect(jsonPath("$.email").value("ivanov@example.com"));
    }

    @Test
    @DisplayName("Получить пользователя по ID. Не существует")
    void getUserById_NotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", INVALID_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить пользователя по ID. Нулевой ID")
    void getUserById_NullId() throws Exception {
        mockMvc.perform(get("/users/{id}", ""))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить всех пользователей. Успешно")
    void getAllUsers_Success() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    @DisplayName("Удалить пользователя. Успешно")
    void deleteUser_Success() throws Exception {
        mockMvc.perform(delete("/users/{id}", USER_ID_3))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}", USER_ID_3))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Удалить пользователя. Не существует")
    void deleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/users/{id}", INVALID_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Удалить пользователя. Нулевой ID")
    void deleteUser_NullId() throws Exception {
        mockMvc.perform(delete("/users/{id}", ""))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Обновить пользователя. Пустое имя")
    void updateUser_EmptyName() throws Exception {
        UserDto dto = new UserDto();
        dto.setName(""); // пустое имя
        dto.setEmail("updated@example.com");

        mockMvc.perform(patch("/users/{id}", USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(""));
    }

    @Test
    @DisplayName("Обновить пользователя. Только имя")
    void updateUser_OnlyName() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Только имя");

        mockMvc.perform(patch("/users/{id}", USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Только имя"))
                .andExpect(jsonPath("$.email").isString());
    }

    @Test
    @DisplayName("Обновить пользователя. Только email")
    void updateUser_OnlyEmail() throws Exception {
        UserDto dto = new UserDto();
        dto.setEmail("onlyemail@example.com");

        mockMvc.perform(patch("/users/{id}", USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("onlyemail@example.com"))
                .andExpect(jsonPath("$.name").isString());
    }

    @Test
    @DisplayName("Создать пользователя. Пустое имя")
    void createUser_EmptyName() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("");
        dto.setEmail("emptyname@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(""));
    }

    @Test
    @DisplayName("Удалить пользователя. Есть связанные данные")
    void deleteUser_WithRelatedData() throws Exception {
        // В data.sql пользователь с id=1 имеет вещь и запрос
        mockMvc.perform(delete("/users/{id}", USER_ID_1))
                .andExpect(status().isOk());

        // Проверяем, что пользователь удален
        mockMvc.perform(get("/users/{id}", USER_ID_1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получить всех пользователей. База данных пустая")
    void getAllUsers_EmptyDatabase() throws Exception {
        // Очистка перед тестом
        mockMvc.perform(delete("/users/{id}", USER_ID_1));
        mockMvc.perform(delete("/users/{id}", USER_ID_2));
        mockMvc.perform(delete("/users/{id}", USER_ID_3));
        mockMvc.perform(delete("/users/{id}", USER_ID_4));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}