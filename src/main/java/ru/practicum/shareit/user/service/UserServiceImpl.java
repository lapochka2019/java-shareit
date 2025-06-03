package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User create(UserDto userDto) {
        log.info("Проверяем, нет ли такого пользователя");
        checkUserNotExist(userDto.getEmail());
        log.info("Создаем нового пользователя пользователя");
        return userRepository.save(userMapper.toEntity(userDto));
    }

    @Override
    public User update(UserDto userDto, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id не может быть null");
        }
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID=" + id + " не найден"));

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        return userRepository.save(existingUser);
    }

    @Override
    public User getUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id не может быть null");
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден");
        } else {
            return user.get();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }

    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id не может быть null");
        }
        try {
            log.info("Проверяем, существует ли пользователь");
            checkUserExist(id);
            log.info("Удаляем пользователя");
            userRepository.deleteById(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден");
        }
    }

    public void checkUserNotExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Пользователь с email " + email + " уже существует");
        }
    }

    @Override
    public void checkUserExist(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }
}
