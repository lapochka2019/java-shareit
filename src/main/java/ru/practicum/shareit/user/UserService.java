package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(User user);

    UserDto update(UserDto user, int id);

    UserDto getUser(int id);

    List<UserDto> getAllUsers();

    void delete(int id);
}
