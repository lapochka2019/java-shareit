package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тесты для проверки UserService")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userStorage;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(1, "User Name", "user@mail.ru");
    }

    @Test
    @DisplayName("Создание и обновление пользователя успешно")
    void testUpdateUser_Success() {
        UserDto created = userService.create(user);
        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("User Name");

        created.setEmail("Update@mail.ru");
        UserDto updated = userService.update(created, user.getId());

        assertThat(updated).isNotNull();
        assertThat(updated.getEmail()).isEqualTo("Update@mail.ru");
    }

    @Test
    @DisplayName("Обновление пользователя. Пользователь не найден")
    void testUpdateUser_NotFound() {
        user.setId(999);

        assertThatThrownBy(() -> userService.update(UserMapper.toUserDto(user), user.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Пользователь с id 999 не найден");
    }

    @Test
    @DisplayName("Получение пользователя. Успешно")
    void testGetUser_Success() {
        userService.create(user);
        UserDto found = userService.getUser(user.getId());

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("User Name");
    }

    @Test
    @DisplayName("Получение пользователя. Несуществующий пользователь")
    void testGetUser_NotFound() {
        assertThatThrownBy(() -> userService.getUser(999))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Пользователь с id 999 не найден");
    }

    @Test
    @DisplayName("Получение всех пользователей успешно")
    void testGetAllUsers_Success() {
        // Создаем двух пользователей
        UserDto created1 = userService.create(user);
        User user2 = new User(0, "Second Name", "second@mail.ru");
        UserDto created2 = userService.create(user2);

        List<UserDto> allUsers = userService.getAllUsers();

        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).contains(created1, created2);
    }

    @Test
    @DisplayName("Удаление пользователя успешно")
    void testDeleteUser_Success() {
        UserDto created = userService.create(user);
        userService.delete(user.getId());

        assertThatThrownBy(() -> userService.getUser(user.getId()))
                .isInstanceOf(NotFoundException.class);
    }
}
