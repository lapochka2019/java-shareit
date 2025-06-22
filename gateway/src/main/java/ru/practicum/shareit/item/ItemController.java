package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.utils.Marker;

import static ru.practicum.shareit.utils.Constants.USER_ID_HEADER;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) Long owner,
                                         @Valid @RequestBody ItemCreateDto itemCreateDto) {
        log.info("Запрос на создание вещи {} от пользователя {}", itemCreateDto, owner);
        return itemClient.create(owner, itemCreateDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader(USER_ID_HEADER) Long owner,
                                         @Valid @RequestBody ItemCreateDto itemCreateDto, @PathVariable("id") Long id) {
        log.info("Запрос на обновление вещи {} от пользователя {}", itemCreateDto, owner);
        return itemClient.update(id, owner, itemCreateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable("id") Long id) {
        log.info("Запрос на получение вещи с id {}", id);
        return itemClient.getItem(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsForOwner(@RequestHeader(USER_ID_HEADER) Long owner) {
        log.info("Запрос на получение вещей пользователя id {}", owner);
        return itemClient.findAll(owner);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(USER_ID_HEADER) Long userId, @RequestParam String text) {
        log.info("Поиск вещей по запросу: {}", text);
        return itemClient.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@RequestHeader(USER_ID_HEADER) Long user,
                                                   @PathVariable("itemId") Long itemId, @Valid @RequestBody CommentDto commentDto) {
        log.info("Пользователь: {} пытается оставить комментарий \"{}\" для вещи с id {}", user, commentDto, itemId);
        return itemClient.createComment(user, commentDto, itemId);
    }
}
