package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.USER_ID_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID_HEADER) Long owner,
                          @RequestBody ItemCreateDto itemCreateDto) {
        log.info("Запрос на создание вещи {} от пользователя {}", itemCreateDto, owner);
        return service.create(itemCreateDto, owner);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) Long owner,
                          @RequestBody ItemCreateDto itemCreateDto, @PathVariable("id") Long id) {
        log.info("Запрос на обновление вещи {} от пользователя {}", itemCreateDto, owner);
        return service.update(itemCreateDto, owner, id);
    }

    @GetMapping("/{id}")
    public ItemFullDto getItem(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable("id") Long id) {
        log.info("Запрос на получение вещи с id {}", id);
        return service.getItem(id, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsForOwner(@RequestHeader(USER_ID_HEADER) Long owner) {
        log.info("Запрос на получение вещей пользователя id {}", owner);
        return service.getItemsForOwner(owner);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Поиск вещей по запросу: {}", text);
        return service.itemSearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addCommentToItem(@RequestHeader(USER_ID_HEADER) Long user,
                                               @PathVariable("itemId") Long itemId, @Valid @RequestBody CommentDto commentDto) {
        log.info("Пользователь: {} пытается оставить комментарий \"{}\" для вещи с id {}", user, commentDto, itemId);
        return service.addCommentToItem(user, itemId, commentDto);
    }
}