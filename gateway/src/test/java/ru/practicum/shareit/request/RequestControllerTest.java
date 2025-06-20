package ru.practicum.shareit.request;

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
public class RequestControllerTest {
    @Mock
    private RequestClient requestClient;

    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    @DisplayName("Создание запроса")
    void createRequest_ReturnsResponseFromClient() {
        Long userId = 1L;
        ItemRequestDto dto = new ItemRequestDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Created");

        when(requestClient.create(userId, dto)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemRequestController.create(userId, dto);

        assertEquals(expectedResponse, actualResponse);
        verify(requestClient, times(1)).create(userId, dto);
    }

    @Test
    @DisplayName("Получение своих запросов")
    void getUserRequests_ReturnsResponseFromClient() {
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("User requests");

        when(requestClient.getUserRequests(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemRequestController.getUserRequests(userId);

        assertEquals(expectedResponse, actualResponse);
        verify(requestClient, times(1)).getUserRequests(userId);
    }

    @Test
    @DisplayName("Получение всех запросов")
    void getAllRequests_ReturnsResponseFromClient() {
        Long userId = 1L;
        Integer offset = 0;
        Integer limit = 10;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("All requests");

        when(requestClient.getAllRequests(userId, limit, offset)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemRequestController.getAllRequests(userId, offset, limit);

        assertEquals(expectedResponse, actualResponse);
        verify(requestClient, times(1)).getAllRequests(userId, limit, offset);
    }

    @Test
    @DisplayName("Получение запроса по ID")
    void getRequestById_ReturnsResponseFromClient() {
        Long requestId = 100L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Request data");

        when(requestClient.getRequestById(requestId)).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemRequestController.getRequestById(requestId);

        assertEquals(expectedResponse, actualResponse);
        verify(requestClient, times(1)).getRequestById(requestId);
    }
}
