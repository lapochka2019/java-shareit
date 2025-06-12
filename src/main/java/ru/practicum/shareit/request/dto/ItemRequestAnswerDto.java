package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestAnswerDto {
    Long id;
    String description;
    LocalDateTime created;
    List<ItemDtoForRequest> items;
}
