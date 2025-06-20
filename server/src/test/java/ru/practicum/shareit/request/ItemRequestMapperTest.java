package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    @Test
    @DisplayName("Конвертация ItemRequestDto в ItemRequest с заполненными данными")
    void testToItemRequest_withAllFieldsFilled() {
        ItemRequestDto dto = new ItemRequestDto("Need a chair");
        Long id = 1L;
        Long requesterId = 10L;
        LocalDateTime created = LocalDateTime.now();
        List<Item> items = List.of(
                new Item(1L, "Chair", "A wooden chair", true, null, null),
                new Item(2L, "Table", "A small table", true, null, null)
        );

        ItemRequest request = ItemRequestMapper.INSTANCE.toItemRequest(id, dto, requesterId, created, items);

        assertThat(request).isNotNull();
        assertThat(request.getId()).isEqualTo(id);
        assertThat(request.getDescription()).isEqualTo(dto.getDescription());
        assertThat(request.getRequesterId()).isEqualTo(requesterId);
        assertThat(request.getCreated()).isEqualTo(created);
        assertThat(request.getItems()).isEqualTo(items);
    }

    @Test
    @DisplayName("Конвертация ItemRequest с null значениями")
    void testToItemRequest_withNullValues() {
        ItemRequest request = ItemRequestMapper.INSTANCE.toItemRequest(null, null, null, null, null);

        assertThat(request).isNull();
    }

    @Test
    @DisplayName("Конвертация ItemRequest в ItemRequestAnswerDto с данными")
    void testToItemRequestAnswerDto_withValidData() {
        LocalDateTime now = LocalDateTime.now();
        List<ItemDtoForRequest> items = List.of(
                new ItemDtoForRequest(1L, "Chair", 10L),
                new ItemDtoForRequest(2L, "Table", 10L)
        );

        ItemRequest request = new ItemRequest(1L, "Need a chair", 10L, now, null);

        ItemRequestAnswerDto answerDto = ItemRequestMapper.INSTANCE.toItemRequestAnswerDto(request, items);

        assertThat(answerDto).isNotNull();
        assertThat(answerDto.getId()).isEqualTo(request.getId());
        assertThat(answerDto.getDescription()).isEqualTo(request.getDescription());
        assertThat(answerDto.getCreated()).isEqualTo(request.getCreated());
        assertThat(answerDto.getItems()).isEqualTo(items);
    }

    @Test
    @DisplayName("Конвертация ItemRequest в ItemRequestAnswerDto с null ссылками")
    void testToItemRequestAnswerDto_withNullReferences() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Some description");
        request.setCreated(LocalDateTime.now());
        request.setRequesterId(10L);
        request.setItems(null);

        List<ItemDtoForRequest> items = null;

        ItemRequestAnswerDto answerDto = ItemRequestMapper.INSTANCE.toItemRequestAnswerDto(request, items);

        assertThat(answerDto).isNotNull();
        assertThat(answerDto.getId()).isEqualTo(1L);
        assertThat(answerDto.getDescription()).isEqualTo("Some description");
        assertThat(answerDto.getCreated()).isEqualTo(request.getCreated());
        assertThat(answerDto.getItems()).isNull();
    }

    @Test
    @DisplayName("Конвертация ItemRequest в ItemRequestAnswerDto с пустым списком")
    void testToItemRequestAnswerDto_withEmptyItemsList() {
        List<ItemDtoForRequest> items = List.of();

        ItemRequest request = new ItemRequest(1L, "Description", 10L, LocalDateTime.now(), null);

        ItemRequestAnswerDto answerDto = ItemRequestMapper.INSTANCE.toItemRequestAnswerDto(request, items);

        assertThat(answerDto.getItems()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Конвертация ItemRequest в ItemRequestAnswerDto с минимальной датой")
    void testToItemRequestAnswerDto_withMinDate() {
        LocalDateTime minDate = LocalDateTime.MIN;

        ItemRequest request = new ItemRequest(1L, "Min date request", 10L, minDate, null);
        List<ItemDtoForRequest> items = List.of(new ItemDtoForRequest(1L, "Item", 10L));

        ItemRequestAnswerDto answerDto = ItemRequestMapper.INSTANCE.toItemRequestAnswerDto(request, items);

        assertThat(answerDto.getCreated()).isEqualTo(minDate);
    }

    @Test
    @DisplayName("Конвертация ItemRequest в ItemRequestAnswerDto с максимальной датой")
    void testToItemRequestAnswerDto_withMaxDate() {
        LocalDateTime maxDate = LocalDateTime.MAX;

        ItemRequest request = new ItemRequest(1L, "Max date request", 10L, maxDate, null);
        List<ItemDtoForRequest> items = List.of(new ItemDtoForRequest(1L, "Item", 10L));

        ItemRequestAnswerDto answerDto = ItemRequestMapper.INSTANCE.toItemRequestAnswerDto(request, items);

        assertThat(answerDto.getCreated()).isEqualTo(maxDate);
    }
}
