package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос на создание пользователя {}", user);
        return service.create(user);
    }

    @PatchMapping("/{id}")
    public User update(@Valid @RequestBody UserDto userDto, @PathVariable("id") Long id) {
        log.info("Запрос на обновление пользователя {} c id {}", userDto, id);
        return service.update(userDto, id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        log.info("Запрос на получение пользователя с id {}", id);
        return service.getUser(id);
    }

    @GetMapping()
    public List<User> getUser() {
        log.info("Запрос на получение всех пользователей");
        return service.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Запрос на удаление пользователя c id {}", id);
        service.delete(id);
    }
}
