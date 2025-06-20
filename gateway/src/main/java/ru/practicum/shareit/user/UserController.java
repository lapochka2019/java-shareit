package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.utils.Marker;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @Validated(Marker.OnCreate.class)
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody UserDto userDto, @PathVariable("id") Long id) {
        log.info("Запрос на обновление пользователя {} c id {}", userDto, id);
        return userClient.update(id, userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long id) {
        log.info("Запрос на получение пользователя с id {}", id);
        return userClient.getById(id);
    }

    @GetMapping()
    public ResponseEntity<Object> getUser() {
        log.info("Запрос на получение всех пользователей");
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Запрос на удаление пользователя c id {}", id);
        userClient.deleteById(id);
    }
}
