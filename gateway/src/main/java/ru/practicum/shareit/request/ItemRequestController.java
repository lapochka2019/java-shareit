package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.utils.Marker;

import static ru.practicum.shareit.utils.Constants.USER_ID_HEADER;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Запрос на создание Request {} от пользователя {}", itemRequestDto, userId);
        return requestClient.create(userId, itemRequestDto);
    }

    @GetMapping()
    public ResponseEntity<Object> getUserRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Запрос на получение Request'ов пользователя с id {}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @RequestParam(defaultValue = "0") Integer offset,
                                                 @RequestParam(defaultValue = "10") Integer limit) {
        log.info("Запрос на получение всех Request'ов");
        return requestClient.getAllRequests(userId, limit, offset);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestById(@PathVariable("id") Long id) {
        log.info("Запрос на получение Request'а с id {}", id);
        return requestClient.getRequestById(id);
    }
}
