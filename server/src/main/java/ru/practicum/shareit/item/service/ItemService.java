package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemCreateDto itemCreateDto, Long owner);

    ItemDto update(ItemCreateDto itemCreateDto, Long owner, Long id);

    ItemFullDto getItem(Long itemId, Long userId);

    List<ItemDto> getItemsForOwner(Long owner);

    List<ItemDto> itemSearch(String text);

    Item checkItemExist(Long id);

    CommentResponseDto addCommentToItem(Long id, Long item, CommentDto commentDto);
}
