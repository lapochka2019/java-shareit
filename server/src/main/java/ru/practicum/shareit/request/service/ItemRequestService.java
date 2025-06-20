package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest create(ItemRequestDto dto, Long user);

    List<ItemRequestAnswerDto> getUserRequests(Long user);

    List<ItemRequestAnswerDto> getAllRequests(int limit, int offset);

    ItemRequestAnswerDto getRequest(Long requestId);
}
