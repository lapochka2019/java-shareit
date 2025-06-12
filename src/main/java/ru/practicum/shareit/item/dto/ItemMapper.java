package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toItemDto(Item item);

    Item dtoToItem(ItemDto item, Long id, Long owner);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "request", source = "item.request.id")
    ItemFullDto toFullItem(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> comments, UserDto owner);

    ItemDtoForRequest toItemDtoForRequest (Item item);
}

