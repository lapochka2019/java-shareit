package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя {}", userDto);
        return service.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable("id") Long id) {
        log.info("Запрос на обновление пользователя {} c id {}", userDto, id);
        return service.update(userDto, id);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        log.info("Запрос на получение пользователя с id {}", id);
        return service.getUser(id);
    }

    @GetMapping()
    public List<UserDto> getUser() {
        log.info("Запрос на получение всех пользователей");
        return service.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Запрос на удаление пользователя c id {}", id);
        service.delete(id);
    }
}