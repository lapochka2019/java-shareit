package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserDto userDto;

    @BeforeEach
    void initEach() {
        userDto = new UserDto(1L, "Alice", "alice@example.com");
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Создание пользователя: успешно")
    void create_UserCreatedSuccessfully() {
        UserDto created = userService.create(userDto);

        assertNotNull(created.getId());
        assertEquals(userDto.getName(), created.getName());
        assertEquals(userDto.getEmail(), created.getEmail());

        assertNotNull(userRepository.findById(created.getId()));
    }

    @Test
    @DisplayName("Создание пользователя: email уже занят")
    void create_EmailAlreadyExists_ThrowsConflictException() {
        userService.create(userDto);
        assertThrows(ResponseStatusException.class, () -> userService.create(userDto));
    }

    @Test
    @DisplayName("Обновление пользователя: успешно")
    void update_UserUpdatedSuccessfully() {
        UserDto created = userService.create(userDto);
        UserDto updated = new UserDto(null, "NewName", "new_email@example.com");

        UserDto result = userService.update(updated, created.getId());

        assertNotNull(result);
        assertEquals("NewName", result.getName());
        assertEquals("new_email@example.com", result.getEmail());
    }

    @Test
    @DisplayName("Обновление пользователя: пользователь не найден")
    void update_UserNotFound_ThrowsNotFoundException() {
        Long nonExistentId = 999L;
        UserDto updated = new UserDto(null, "NewName", "new_email@example.com");

        assertThrows(NotFoundException.class, () -> userService.update(updated, nonExistentId));
    }

    @Test
    @DisplayName("Получение пользователя по ID: успешно")
    void getUser_Successfully() {
        UserDto created = userService.create(userDto);

        UserDto result = userService.getUser(created.getId());

        assertNotNull(result);
        assertEquals(created, result);
    }

    @Test
    @DisplayName("Получение пользователя: пользователь не найден")
    void getUser_NotFound_ThrowsNotFoundException() {
        Long nonExistentId = 999L;
        assertThrows(NotFoundException.class, () -> userService.getUser(nonExistentId));
    }

    @Test
    @DisplayName("Получение всех пользователей: успешно")
    void getAllUsers_Successfully() {
        int initialSize = userService.getAllUsers().size();

        userService.create(new UserDto(0L, "User1", "user1@example.com"));
        userService.create(new UserDto(0L, "User2", "user2@example.com"));

        List<UserDto> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertThat(allUsers.size()).isEqualTo(initialSize + 2);
    }

    @Test
    @DisplayName("Удаление пользователя: успешно")
    void delete_UserDeletedSuccessfully() {
        UserDto created = userService.create(userDto);

        userService.delete(created.getId());

        assertThrows(NotFoundException.class, () -> userService.getUser(created.getId()));
    }

    @Test
    @DisplayName("Удаление пользователя: пользователь не найден")
    void delete_UserNotFound_ThrowsNotFoundException() {
        Long nonExistentId = 999L;
        assertThrows(NotFoundException.class, () -> userService.delete(nonExistentId));
    }
}