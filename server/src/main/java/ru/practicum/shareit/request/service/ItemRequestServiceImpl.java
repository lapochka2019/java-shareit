package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequest create(ItemRequestDto dto, Long user) {
        log.info("Проверяем существование пользователя {}", user);
        userService.getUser(user);
        log.info("Создаем запрос");
        return itemRequestRepository.save(itemRequestMapper.toItemRequest(0L, dto, user, LocalDateTime.now(), null));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestAnswerDto> getUserRequests(Long user) {
        userService.getUser(user);

        return itemRequestRepository.findByRequesterIdOrderByCreatedDesc(user).stream().map(request -> itemRequestMapper.toItemRequestAnswerDto(request, Optional.ofNullable(request.getItems()).orElse(Collections.emptyList()).stream().map(itemMapper::toItemDtoForRequest).toList())).toList();
    }

    @Override
    public List<ItemRequestAnswerDto> getAllRequests(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return itemRequestRepository.findAllByOrderByCreatedDesc(pageable).stream().map(request -> itemRequestMapper.toItemRequestAnswerDto(request, Optional.ofNullable(request.getItems()).orElse(Collections.emptyList()).stream().map(itemMapper::toItemDtoForRequest).toList())).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestAnswerDto getRequest(Long requestId) {
        Optional<ItemRequest> optionalItemRequest = itemRequestRepository.findById(requestId);
        if (optionalItemRequest.isEmpty()) {
            throw new NotFoundException("Запрос с ID=" + requestId + " не найден");
        }
        ItemRequest itemRequest = optionalItemRequest.get();

        return itemRequestMapper.toItemRequestAnswerDto(itemRequest, Optional.ofNullable(itemRequest.getItems()).orElse(Collections.emptyList()).stream().map(itemMapper::toItemDtoForRequest).toList());
    }
}
