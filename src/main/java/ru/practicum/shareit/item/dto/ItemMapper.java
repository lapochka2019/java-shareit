package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toItemDto(Item item);
    Item dtoToItem(ItemDto item, Long id, Long owner);
    ItemFullDto toNewItem(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> commentDtoList);
    Item fullItemDtoToItem(ItemFullDto dto);

    }

