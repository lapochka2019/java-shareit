//package ru.practicum.shareit.items;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.jdbc.Sql;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.service.ItemService;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.user.User;
//import ru.practicum.shareit.user.service.UserService;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Создание таблиц
//@DisplayName("Тесты для проверки ItemService")
//public class ItemServiceTest {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private ItemService itemService;
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
//    void testCreateItem_Success() {
//        Item item = itemService.create(itemDto, 1L);
//
//        assertNotNull(item);
//        assertEquals(1, item.getId());
//        assertEquals("Item1", item.getName());
//    }
//
//    @Test
//    @DisplayName("Создание вещи: пользователь не найден")
//    void testCreateItem_UserNotFound() {
//        assertThrows(NotFoundException.class, () -> itemService.create(new ItemDto(), -1L));
//    }
//
//    @Test
//    @DisplayName("Обновление вещи: пользователь не указан")
//    void testUpdateItem_OwnerIsNull() {
//        itemService.create(itemDto, 1L);
//        ItemDto dto = new ItemDto();
//        dto.setName("Новое имя");
//
//        assertThrows(NullPointerException.class, () -> itemService.update(dto, null, 1L));
//    }
//
//    @Test
//    @DisplayName("Обновление вещи: вещь не найдена")
//    void testUpdateItem_ItemNotFound() {
//        assertThrows(IllegalArgumentException.class, () -> itemService.update(itemDto, 1L, 999L));
//    }
//
//    @Test
//    @DisplayName("Получение вещи: успех")
//    void testGetItem_Success() {
//        Item created = itemService.create(itemDto, 1L);
//
//        Item found = itemService.getItem(created.getId());
//
//        assertNotNull(found);
//        assertEquals(created.getId(), found.getId());
//    }
//
//    @Test
//    @DisplayName("Получение вещи: не найдена")
//    void testGetItem_NotFound() {
//        assertThrows(IllegalArgumentException.class, () -> itemService.getItem(999L));
//    }
//
//    @Test
//    @DisplayName("Поиск вещей: пустой запрос возвращает пустой список")
//    void testItemSearch_EmptyText() {
//        List<Item> result = itemService.itemSearch("");
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Получение списка вещей: владелец не указан")
//    void testGetItemsForOwner_OwnerIsNull() {
//        assertThrows(NotFoundException.class, () -> itemService.getItemsForOwner(null));
//    }
//}
