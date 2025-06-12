package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequest toItemRequest (ItemRequestDto itemRequestDto, Long requestorId, LocalDateTime created);

    ItemRequestAnswerDto toItemRequestAnswerDto (ItemRequest itemRequest, List<ItemDtoForRequest> items);
}
