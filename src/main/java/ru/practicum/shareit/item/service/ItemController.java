package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utils.Marker;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public Item create(@RequestHeader("X-Sharer-User-Id") Long owner,
                       @Valid @RequestBody ItemDto itemDto) {
        log.info("Запрос на создание вещи {} от пользователя {}", itemDto, owner);
        return service.create(itemDto, owner);
    }

    @PatchMapping("/{id}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long owner,
                       @Valid @RequestBody ItemDto itemDto, @PathVariable("id") Long id) {
        log.info("Запрос на обновление вещи {} от пользователя {}", itemDto, owner);
        return service.update(itemDto, owner, id);
    }

    @GetMapping("/{id}")
    public NewItem getItem(@PathVariable("id") Long id) {
        log.info("Запрос на получение вещи с id {}", id);
        return service.getNewItem(id);
    }

    @GetMapping
    public List<Item> getItemsForOwner(@RequestHeader("X-Sharer-User-Id") Long owner) {
        log.info("Запрос на получение вещей пользователя id {}", owner);
        return service.getItemsForOwner(owner);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text) {
        log.info("Поиск вещей по запросу: {}", text);
        return service.itemSearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addCommentToItem(@RequestHeader("X-Sharer-User-Id") Long user,
                                               @PathVariable("itemId") Long itemId, @Valid @RequestBody CommentDto commentDto) {
        log.info("Пользователь: {} пытается оставить комментарий \"{}\" для вещи с id {}", user, commentDto, itemId);
        return service.addCommentToItem(user, itemId, commentDto);
    }
}
