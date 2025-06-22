package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequest toItemRequest(Long id, ItemRequestDto itemRequestDto, Long requesterId, LocalDateTime created, List<Item> items);

    @Mapping(target = "items", source = "items")
    ItemRequestAnswerDto toItemRequestAnswerDto(ItemRequest request, List<ItemDtoForRequest> items);
}
