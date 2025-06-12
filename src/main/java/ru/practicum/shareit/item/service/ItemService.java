package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(ItemDto itemDto, Long owner);

    Item update(ItemDto itemDto, Long owner, Long id);

    ItemFullDto getItem(Long itemId, Long userId);

    List<Item> getItemsForOwner(Long owner);

    List<Item> itemSearch(String text);

    Item checkItemExist(Long id);

    CommentResponseDto addCommentToItem(Long id, Long item, CommentDto commentDto);
}
