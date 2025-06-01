package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User create(User user);

    User update(UserDto user, Long id);

    User getUser(Long id);

    List<User> getAllUsers();

    void delete(Long id);

    void checkUserExist(Long id);
}
