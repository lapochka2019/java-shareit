package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest()
        );
    }

    public static Item toItem(ItemDto item, Long id, Long owner) {
        return new Item(
                id,
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                owner,
                item.getRequest()
        );
    }

    public static ItemFullDto toNewItem(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> commentDtoList) {
        return new ItemFullDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                commentDtoList
        );
    }
}
