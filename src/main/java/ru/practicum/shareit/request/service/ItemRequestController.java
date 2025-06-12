package ru.practicum.shareit.request.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Marker;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ItemRequest create(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Запрос на создание запроса {} от пользователя {}", itemRequestDto, userId);
        return service.create(itemRequestDto, userId);
    }

   @GetMapping()
    public List<ItemRequestAnswerDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение запросов пользователя с id {}", userId);
        return service.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestAnswerDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "0") Integer offset,
                                                     @RequestParam(defaultValue = "10") Integer limit) {
        log.info("Запрос на получение всех запросов");
        return service.getAllRequests(limit, offset);
    }

    @GetMapping("/{id}")
    public ItemRequestAnswerDto getRequestById(@PathVariable("id") Long id) {
        log.info("Запрос на получение запроса с id {}", id);
        return service.getRequest(id);
    }
}
