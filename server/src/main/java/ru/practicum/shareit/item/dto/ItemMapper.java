package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "request", source = "request")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "id", source = "id")
    Item dtoToItem(ItemCreateDto item, Long id, Long owner, ItemRequest request);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "request", source = "item.request.id")
    ItemFullDto toFullItem(Item item, BookingCreationDto lastBooking, BookingCreationDto nextBooking, List<CommentDto> comments, UserDto owner);

    ItemDtoForRequest toItemDtoForRequest(Item item);

    @Mapping(target = "requestId", source = "item.request.id")
    ItemDto toItemDto(Item item);
}

