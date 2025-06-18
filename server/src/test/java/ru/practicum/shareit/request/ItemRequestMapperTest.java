package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    private final ItemRequestMapper mapper = ItemRequestMapper.INSTANCE;

    @Test
    void toItemRequest_shouldMapDtoToEntity() {
        String description = "Need some items";
        Long requesterId = 1L;
        LocalDateTime now = LocalDateTime.now();

        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription(description);

        ItemRequest itemRequest = mapper.toItemRequest(1L, dto, requesterId, now, null);

        assertThat(itemRequest).isNotNull();
        assertThat(itemRequest.getDescription()).isEqualTo(description);
        assertThat(itemRequest.getRequesterId()).isEqualTo(requesterId);
        assertThat(itemRequest.getCreated()).isEqualTo(now);
        assertThat(itemRequest.getItems()).isNull();
    }

    @Test
    void toItemRequestAnswerDto_shouldMapEntityToAnswerDto() {
        LocalDateTime now = LocalDateTime.now();
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need some items");
        request.setCreated(now);

        ItemDtoForRequest item1 = new ItemDtoForRequest(1L, "Screwdriver", 1L);
        ItemDtoForRequest item2 = new ItemDtoForRequest(2L, "Hammer", 2L);
        List<ItemDtoForRequest> itemDtos = List.of(item1, item2);

        ItemRequestAnswerDto answerDto = mapper.toItemRequestAnswerDto(request, itemDtos);

        assertThat(answerDto.getId()).isEqualTo(1L);
        assertThat(answerDto.getDescription()).isEqualTo("Need some items");
        assertThat(answerDto.getCreated()).isEqualTo(now);
        assertThat(answerDto.getItems()).hasSize(2);
        assertThat(answerDto.getItems().get(0)).isEqualTo(item1);
        assertThat(answerDto.getItems().get(1)).isEqualTo(item2);
    }
}
