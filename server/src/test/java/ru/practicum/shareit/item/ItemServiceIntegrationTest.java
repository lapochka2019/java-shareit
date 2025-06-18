package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentsRepository;
import ru.practicum.shareit.item.service.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    private Long ownerId;
    private Long requestId;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
        bookingRepository.deleteAll();
        commentsRepository.deleteAll();

        User user = new User();
        user.setName("Owner");
        user.setEmail("owner@example.com");
        ownerId = userRepository.save(user).getId();

        ItemRequest request = new ItemRequest();
        request.setDescription("Need a test item");
        request.setRequesterId(ownerId);
        request.setCreated(LocalDateTime.now());
        requestId = itemRequestRepository.save(request).getId();
    }

    @DisplayName("Создание предмета с запросом")
    @Test
    void createItemWithRequest() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("NewItem");
        dto.setDescription("Description");
        dto.setAvailable(true);
        dto.setRequestId(requestId);

        ItemDto result = itemService.create(dto, ownerId);
        assertNotNull(result.getId());
        assertEquals("NewItem", result.getName());
        assertEquals(requestId, result.getRequestId());
    }

    @DisplayName("Ошибка: запрос не найден при создании предмета")
    @Test
    void createItem_WithNonExistentRequestId_ThrowsNotFoundException() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("NewItem");
        dto.setDescription("Description");
        dto.setAvailable(true);
        dto.setRequestId(999L); // Не существующий ID запроса

        assertThrows(NotFoundException.class, () -> {
            itemService.create(dto, ownerId);
        }, "Ожидается исключение NotFoundException с сообщением 'Запрос не найден'");
    }

    @DisplayName("Создание предмета без запроса")
    @Test
    void createItemWithoutRequest() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("NoRequestItem");
        dto.setDescription("No Request");
        dto.setAvailable(true);

        ItemDto result = itemService.create(dto, ownerId);
        assertNotNull(result.getId());
        assertNull(result.getRequestId());
    }

    @DisplayName("Ошибка: пользователь не найден при создании")
    @Test
    void createItem_UserNotFound_ThrowsNotFoundException() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("NewItem");

        assertThrows(NotFoundException.class, () -> itemService.create(dto, 999L));
    }

    @DisplayName("Обновление предмета")
    @Test
    void updateItem() {
        Item item = new Item();
        item.setName("OldName");
        item.setDescription("OldDesc");
        item.setAvailable(false);
        item.setOwner(ownerId);
        Long id = itemRepository.save(item).getId();

        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("NewName");
        dto.setDescription("NewDesc");
        dto.setAvailable(true);

        ItemDto result = itemService.update(dto, ownerId, id);
        assertEquals("NewName", result.getName());
        assertEquals("NewDesc", result.getDescription());
        assertTrue(result.getAvailable());
    }

    @DisplayName("Ошибка: обновление чужого предмета")
    @Test
    void updateItem_NotOwner_ThrowsNotFoundException() {
        Item item = new Item(1L, "Item","Description",false, ownerId, null);
        Long id = itemRepository.save(item).getId();

        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("NewName");

        assertThrows(NotFoundException.class, () -> itemService.update(dto, 999L, id));
    }

    @DisplayName("Ошибка: предмет не найден при обновлении")
    @Test
    void updateItem_ItemNotFound_ThrowsNotFoundException() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("NewName");

        assertThrows(NotFoundException.class, () -> itemService.update(dto, ownerId, 999L));
    }

    @DisplayName("Получение предмета владельцем")
    @Test
    void getItemByOwner() {
        Item item = new Item();
        item.setName("TestItem");
        item.setDescription("Desc");
        item.setAvailable(true);
        item.setOwner(ownerId);
        Long itemId = itemRepository.save(item).getId();

        ItemFullDto result = itemService.getItem(itemId, ownerId);
        assertNotNull(result);
        assertEquals(itemId, result.getId());
    }

    @DisplayName("Ошибка: предмет не найден при получении")
    @Test
    void getItem_ItemNotFound_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> itemService.getItem(999L, ownerId));
    }

    @DisplayName("Получение всех предметов владельца")
    @Test
    void getItemsForOwner() {
        Item item1 = new Item();
        item1.setName("Item1");
        item1.setDescription("Desc1");
        item1.setAvailable(true);
        item1.setOwner(ownerId);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Desc2");
        item2.setAvailable(false);
        item2.setOwner(ownerId);
        itemRepository.save(item2);

        List<ItemDto> result = itemService.getItemsForOwner(ownerId);
        assertThat(result).hasSize(2);
    }

    @DisplayName("Поиск предметов по тексту")
    @Test
    void searchItemsByText() {
        Item item1 = new Item();
        item1.setName("Table");
        item1.setDescription("Wooden table");
        item1.setAvailable(true);
        item1.setOwner(ownerId);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Chair");
        item2.setDescription("Plastic chair");
        item2.setAvailable(true);
        item2.setOwner(ownerId);
        itemRepository.save(item2);

        List<ItemDto> result = itemService.itemSearch("table");
        assertThat(result).hasSize(1);
        assertEquals("Table", result.get(0).getName());
    }

    @DisplayName("Добавление комментария к предмету")
    @Test
    void addCommentToItem() {
        User user = new User();
        user.setName("Commenter");
        user.setEmail("commenter@example.com");
        Long userId = userRepository.save(user).getId();

        Item item = new Item();
        item.setName("CommentedItem");
        item.setDescription("Can comment");
        item.setAvailable(true);
        item.setOwner(ownerId);
        Long itemId = itemRepository.save(item).getId();

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        bookingRepository.save(booking);

        CommentResponseDto result = itemService.addCommentToItem(userId, itemId, commentDto);
        assertNotNull(result);
        assertEquals("Great item!", result.getText());
        assertEquals(user.getName(), result.getAuthorName());
    }

    @DisplayName("Ошибка: пользователь не брал вещь в аренду")
    @Test
    void addComment_UserDidNotBookItem_ThrowsValidationException() {
        User user = new User();
        user.setName("Commenter");
        user.setEmail("commenter@example.com");
        Long userId = userRepository.save(user).getId();

        Item item = new Item();
        item.setName("NoBookingItem");
        item.setDescription("No booking allowed");
        item.setAvailable(true);
        item.setOwner(ownerId);
        Long itemId = itemRepository.save(item).getId();

        CommentDto commentDto = new CommentDto();
        commentDto.setText("This should fail");

        assertThrows(ValidationException.class, () ->
                itemService.addCommentToItem(userId, itemId, commentDto));
    }
}