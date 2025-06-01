//package ru.practicum.shareit.items;
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
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.user.User;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
////@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Создание таблиц
//@DisplayName("Тесты для проверки ItemController")
//public class ItemControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private ItemDto itemDto;
//
//    @BeforeEach
//    void setup() {
//        User user = new User(1L, "User Name", "user@mail.ru");
//        itemDto = new ItemDto("Item1", "Description1", true, null);
//    }
//
//    @Test
//    @DisplayName("Создание вещи успешно")
//    void testCreateItem_Success() throws Exception {
//        mockMvc.perform(post("/items")
//                        .header("X-Sharer-User-Id", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(itemDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Item1"))
//                .andExpect(jsonPath("$.description").value("Description1"));
//    }
//
//    @Test
//    @DisplayName("Создание вещи: невалидное name")
//    void testCreateItem_InvalidName() throws Exception {
//        itemDto.setName("");
//        mockMvc.perform(post("/items")
//                        .header("X-Sharer-User-Id", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(itemDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$['create.itemDto.name']").value("name должен быть заполнен"));
//    }
//
//    @Test
//    @DisplayName("Создание вещи: невалидное description")
//    void testCreateItem_InvalidDescription() throws Exception {
//        itemDto.setDescription(null);
//        mockMvc.perform(post("/items")
//                        .header("X-Sharer-User-Id", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(itemDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$['create.itemDto.description']").value("description должен быть заполнен"));
//    }
//
//    @Test
//    @DisplayName("Создание вещи: невалидное available")
//    void testCreateItem_InvalidAvailable() throws Exception {
//        itemDto.setAvailable(null);
//        mockMvc.perform(post("/items")
//                        .header("X-Sharer-User-Id", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(itemDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$['create.itemDto.available']").value("available должен быть заполнен"));
//
//    }
//}
