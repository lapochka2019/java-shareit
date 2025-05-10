package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository storage;

    public UserServiceImpl(UserRepository storage) {
        this.storage = storage;
    }

    @Override
    public UserDto create(User user) {
        log.info("Проверяем, нет ли такого пользователя");
        storage.checkUserExist(user);
        log.info("Создаем нового пользователя пользователя");
        return storage.create(user);
    }

    @Override
    public UserDto update(UserDto userDto, int id) {
        try {
            log.info("Проверяем, существует ли пользователь");
            storage.getUser(id);
            log.info("Пользователь существует, обновляем его данные");
            return storage.update(userDto, id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден");
        }
    }

    @Override
    public UserDto getUser(int id) {
        try {
            return storage.getUser(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        try {
            return storage.getAllUsers();
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }

    }

    @Override
    public void delete(int id) {
        try {
            log.info("Проверяем, существует ли пользователь");
            storage.getUser(id);
            log.info("Удаляем пользователя");
            storage.delete(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден");
        }
    }

}
