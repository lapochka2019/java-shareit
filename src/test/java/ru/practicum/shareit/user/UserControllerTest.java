//package ru.practicum.shareit.user;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Создание таблиц
//@DisplayName("Тесты для проверки UserController")
//@ActiveProfiles("test")
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private User user;
//
//    @BeforeEach
//    void setup() {
//        user = new User(1L, "User Name", "user@mail.ru");
//    }
//
//    @Test
//    @DisplayName("Создание пользователя успешно")
//    void testCreateUser_Success() throws Exception {
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("User Name"));
//    }
//
//    @Test
//    @DisplayName("Создание пользователя: невалидный email")
//    void testCreateUser_InvalidEmail() throws Exception {
//        user.setEmail("email");
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$['error']").value("Это не похоже на email"));
//
//    }
//}
