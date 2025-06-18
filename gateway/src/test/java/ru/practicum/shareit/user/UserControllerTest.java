package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Создание пользователя")
    void createUser_ReturnsResponseFromClient() {
        UserDto dto = new UserDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Created");

        when(userClient.create(dto)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userController.create(dto);

        assertEquals(expectedResponse, actualResponse);
        verify(userClient, times(1)).create(dto);
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateUser_ReturnsResponseFromClient() {
        Long userId = 1L;
        UserDto dto = new UserDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Updated");

        when(userClient.update(userId, dto)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userController.update(dto, userId);

        assertEquals(expectedResponse, actualResponse);
        verify(userClient, times(1)).update(userId, dto);
    }

    @Test
    @DisplayName("Получение пользователя по ID")
    void getUserById_ReturnsResponseFromClient() {
        Long userId = 100L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("User data");

        when(userClient.getById(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userController.getUser(userId);

        assertEquals(expectedResponse, actualResponse);
        verify(userClient, times(1)).getById(userId);
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void getAllUsers_ReturnsResponseFromClient() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("All users");

        when(userClient.getAll()).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userController.getUser();

        assertEquals(expectedResponse, actualResponse);
        verify(userClient, times(1)).getAll();
    }

    @Test
    @DisplayName("Удаление пользователя по ID")
    void deleteUserById_ShouldCallClient() {
        Long userId = 100L;

        userController.delete(userId);

        verify(userClient, times(1)).deleteById(userId);
    }
}
