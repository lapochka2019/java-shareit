package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "Alice", "alice@example.com");
        user = new User(1L, "Alice", "alice@example.com");
    }

    @Test
    @DisplayName("Создание пользователя: успешно")
    void create_UserCreatedSuccessfully() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.create(userDto);

        assertNotNull(result);
        assertEquals(userDto, result);
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userMapper, times(1)).toEntity(userDto);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    @DisplayName("Создание пользователя: email занят")
    void create_EmailAlreadyExists_ThrowsConflictException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.create(userDto));
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
    }

    @Test
    @DisplayName("Обновление пользователя: успешно")
    void update_UserUpdatedSuccessfully() {
        Long id = 1L;
        UserDto updatedDto = new UserDto(null, "NewName", "newemail@example.com");
        User existingUser = new User(id, "OldName", "oldemail@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toDto(existingUser)).thenReturn(userDto);

        UserDto result = userService.update(updatedDto, id);

        assertNotNull(result);
        assertEquals("NewName", existingUser.getName());
        assertEquals("newemail@example.com", existingUser.getEmail());
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(existingUser);
        verify(userMapper, times(1)).toDto(existingUser);
    }

    @Test
    @DisplayName("Обновление пользователя: пользователь не найден")
    void update_UserNotFound_ThrowsNotFoundException() {
        Long id = 1L;
        UserDto updatedDto = new UserDto(null, "NewName", "newemail@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(updatedDto, id));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Обновление пользователя: null ID")
    void update_NullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.update(userDto, null));
    }

    @Test
    @DisplayName("Получение пользователя по ID: успешно")
    void getUser_Successfully() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUser(id);

        assertNotNull(result);
        assertEquals(userDto, result);
        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    @DisplayName("Получение пользователя: пользователь не найден")
    void getUser_NotFound_ThrowsNotFoundException() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(id));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Получение пользователя: null ID")
    void getUser_NullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(null));
    }

    @Test
    @DisplayName("Получение всех пользователей: успешно")
    void getAllUsers_Successfully() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    @DisplayName("Получение всех пользователей: пустой список")
    void getAllUsers_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Удаление пользователя: успешно")
    void delete_UserDeletedSuccessfully() {
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> userService.delete(id));
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Удаление пользователя: пользователь не найден")
    void delete_UserNotFound_ThrowsNotFoundException() {
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.delete(id));
        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, never()).deleteById(id); // <- мы НЕ ожидаем вызова deleteById
    }

    @Test
    @DisplayName("Удаление пользователя: null ID")
    void delete_NullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.delete(null));
    }
}