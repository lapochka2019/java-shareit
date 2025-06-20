package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.USER_ID_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;

    @PostMapping
    public ItemRequest create(@RequestHeader(USER_ID_HEADER) Long userId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Запрос на создание Request {} от пользователя {}", itemRequestDto, userId);
        return service.create(itemRequestDto, userId);
    }

    @GetMapping()
    public List<ItemRequestAnswerDto> getUserRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Запрос на получение Request'ов пользователя с id {}", userId);
        return service.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestAnswerDto> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                     @RequestParam(defaultValue = "0") Integer offset,
                                                     @RequestParam(defaultValue = "10") Integer limit) {
        log.info("Запрос на получение всех Request'ов");
        return service.getAllRequests(limit, offset);
    }

    @GetMapping("/{id}")
    public ItemRequestAnswerDto getRequestById(@PathVariable("id") Long id) {
        log.info("Запрос на получение Request'а с id {}", id);
        return service.getRequest(id);
    }
}