package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    @Test
    @DisplayName("Создание вещи")
    void createItem_ReturnsResponseFromClient() {
        Long ownerId = 1L;
        ItemCreateDto dto = new ItemCreateDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Created");

        when(itemClient.create(ownerId, dto)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemController.create(ownerId, dto);

        assertEquals(expectedResponse, actualResponse);
        verify(itemClient, times(1)).create(ownerId, dto);
    }

    @Test
    @DisplayName("Обновление вещи")
    void updateItem_ReturnsResponseFromClient() {
        Long ownerId = 1L;
        Long itemId = 100L;
        ItemCreateDto dto = new ItemCreateDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Updated");

        when(itemClient.update(itemId, ownerId, dto)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemController.update(ownerId, dto, itemId);

        assertEquals(expectedResponse, actualResponse);
        verify(itemClient, times(1)).update(itemId, ownerId, dto);
    }

    @Test
    @DisplayName("Получение вещи по ID")
    void getItem_ReturnsResponseFromClient() {
        Long userId = 1L;
        Long itemId = 100L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Item Data");

        when(itemClient.getItem(itemId, userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemController.getItem(userId, itemId);

        assertEquals(expectedResponse, actualResponse);
        verify(itemClient, times(1)).getItem(itemId, userId);
    }

    @Test
    @DisplayName("Получение всех вещей пользователя")
    void getItemsForOwner_ReturnsResponseFromClient() {
        Long ownerId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("List of items");

        when(itemClient.findAll(ownerId)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemController.getItemsForOwner(ownerId);

        assertEquals(expectedResponse, actualResponse);
        verify(itemClient, times(1)).findAll(ownerId);
    }

    @Test
    @DisplayName("Поиск вещей по запросу")
    void searchItems_ReturnsResponseFromClient() {
        Long userId = 1L;
        String text = "query";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Search results");

        when(itemClient.searchItems(userId, text)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemController.searchItems(userId, text);

        assertEquals(expectedResponse, actualResponse);
        verify(itemClient, times(1)).searchItems(userId, text);
    }

    @Test
    @DisplayName("Добавление комментария к вещи")
    void addCommentToItem_ReturnsResponseFromClient() {
        Long userId = 1L;
        Long itemId = 100L;
        CommentDto commentDto = new CommentDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Comment added");

        when(itemClient.createComment(userId, commentDto, itemId)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemController.addCommentToItem(userId, itemId, commentDto);

        assertEquals(expectedResponse, actualResponse);
        verify(itemClient, times(1)).createComment(userId, commentDto, itemId);
    }
}