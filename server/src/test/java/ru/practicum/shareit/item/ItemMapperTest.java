package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    @Test
    @DisplayName("Конвертация ItemCreateDto в Item с заполненными данными")
    void testDtoToItem_withAllFieldsFilled() {
        ItemCreateDto dto = new ItemCreateDto("Item", "description", true, 100L);
        Long id = 1L;
        Long owner = 5L;
        ItemRequest request = new ItemRequest(100L, "Need this", 1L, LocalDateTime.now(), null);

        Item item = ItemMapper.INSTANCE.dtoToItem(dto, id, owner, request);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isEqualTo(id);
        assertThat(item.getName()).isEqualTo(dto.getName());
        assertThat(item.getDescription()).isEqualTo(dto.getDescription());
        assertThat(item.getAvailable()).isEqualTo(dto.getAvailable());
        assertThat(item.getRequest()).isEqualTo(request);
        assertThat(item.getOwner()).isEqualTo(owner);
    }

    @Test
    @DisplayName("Конвертация Item с null значениями")
    void testDtoToItem_withNullValues() {
        Item item = ItemMapper.INSTANCE.dtoToItem(null, null, null, null);

        assertThat(item).isNull();
    }

    @Test
    @DisplayName("Конвертация в ItemFullDto со всеми полями")
    void testToFullItem_withAllFields() {
        Item item = new Item(1L, "Item", "desc", true, null, null);
        BookingCreationDto lastBooking = new BookingCreationDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        BookingCreationDto nextBooking = new BookingCreationDto(2L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));
        List<CommentDto> comments = List.of(new CommentDto("Great!"));
        UserDto owner = new UserDto(1L, "Name", "user@example.com");

        ItemFullDto fullDto = ItemMapper.INSTANCE.toFullItem(item, lastBooking, nextBooking, comments, owner);

        assertThat(fullDto).isNotNull();
        assertThat(fullDto.getId()).isEqualTo(item.getId());
        assertThat(fullDto.getName()).isEqualTo(item.getName());
        assertThat(fullDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(fullDto.getAvailable()).isEqualTo(item.getAvailable());
        assertThat(fullDto.getLastBooking()).isEqualTo(lastBooking);
        assertThat(fullDto.getNextBooking()).isEqualTo(nextBooking);
        assertThat(fullDto.getComments()).isEqualTo(comments);
        assertThat(fullDto.getOwner()).isEqualTo(owner);
    }

    @Test
    @DisplayName("Конвертация в ItemFullDto с null значениями")
    void testToFullItem_withNulls() {
        ItemFullDto fullDto = ItemMapper.INSTANCE.toFullItem(null, null, null, null, null);

        assertThat(fullDto).isNull();
    }

    @Test
    @DisplayName("Конвертация Item в ItemDtoForRequest с данными")
    void testToItemDtoForRequest_withValidItem() {
        Item item = new Item(1L, "Item", "desc", true, 10L, null);

        ItemDtoForRequest dto = ItemMapper.INSTANCE.toItemDtoForRequest(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Item");
        assertThat(dto.getOwner()).isEqualTo(item.getOwner());
    }

    @Test
    @DisplayName("Конвертация Item в ItemDtoForRequest с null значениями")
    void testToItemDtoForRequest_withNullItem() {
        Item item = new Item();

        ItemDtoForRequest dto = ItemMapper.INSTANCE.toItemDtoForRequest(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getOwner()).isNull();
    }

    @Test
    @DisplayName("Конвертация Item в ItemDto с requestId")
    void testToItemDto_withRequestId() {
        Item item = new Item(1L, "Item", "desc", true, 5L, null);

        ItemDto dto = ItemMapper.INSTANCE.toItemDto(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Item");
        assertThat(dto.getDescription()).isEqualTo("desc");
        assertThat(dto.getAvailable()).isEqualTo(true);
        assertThat(dto.getOwner()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Конвертация Item в ItemDto без requestId")
    void testToItemDto_withNullRequest() {
        Item item = new Item(1L, "Item", "desc", true, null, null);

        ItemDto dto = ItemMapper.INSTANCE.toItemDto(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getRequestId()).isNull();
    }

    @Test
    @DisplayName("Конвертация null в ItemDtoForRequest")
    void testToItemDtoForRequest_withNullItemId() {
        ItemDtoForRequest itemDtoForRequest = ItemMapper.INSTANCE.toItemDtoForRequest(null);
        assertThat(itemDtoForRequest).isNull();
    }

    @Test
    @DisplayName("Конвертация null в ItemDto")
    void testToItemDto_withNullItemId() {
        ItemDto itemDto = ItemMapper.INSTANCE.toItemDto(null);
        assertThat(itemDto).isNull();
    }
}