package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemMapper itemMapper;

    private LocalDateTime now;
    private UserDto user;
    private Long userId;

    @BeforeEach
    void setUp() {
        itemRequestRepository.deleteAll();
        now = LocalDateTime.now();
        user = new UserDto(1L, "User", "user@mail.ru");
        user = userService.create(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("Создание запроса: пользователь существует")
    void create_whenUserExists_shouldCreateRequest() {
        ItemRequestDto dto = new ItemRequestDto("Need a screwdriver");

        ItemRequest createdRequest = itemRequestService.create(dto, userId);

        assertAll(
                () -> assertThat(createdRequest).isNotNull(),
                () -> assertThat(createdRequest.getDescription()).isEqualTo("Need a screwdriver"),
                () -> assertThat(createdRequest.getRequesterId()).isEqualTo(userId),
                () -> assertThat(createdRequest.getCreated()).isCloseTo(now, within(1, ChronoUnit.SECONDS)),
                () -> assertThat(itemRequestRepository.findById(createdRequest.getId())).isPresent()
        );
    }

    @Test
    @DisplayName("Создание запроса: пользователь не существует")
    void create_whenUserDoesNotExist_shouldThrowNotFoundException() {
        Long invalidUserId = -1L;
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need hammer");

        assertThatThrownBy(() -> itemRequestService.create(dto, invalidUserId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Пользователь с ID=" + invalidUserId + " не найден");
    }

    @Test
    @DisplayName("Получение собственных запросов: есть данные")
    void getUserRequests_whenHasRequests_shouldReturnList() {
        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Request 1");
        request1.setRequesterId(userId);
        request1.setCreated(now.minusDays(2));
        request1.setItems(List.of());

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Request 2");
        request2.setRequesterId(userId);
        request2.setCreated(now.minusDays(1));
        request2.setItems(List.of());

        itemRequestRepository.saveAll(List.of(request1, request2));

        List<ItemRequestAnswerDto> result = itemRequestService.getUserRequests(userId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDescription()).isEqualTo("Request 2");
        assertThat(result.get(1).getDescription()).isEqualTo("Request 1");
    }

    @Test
    @DisplayName("Получение собственных запросов: пользователь не существует")
    void getUserRequests_whenUserNotFound_shouldThrowNotFoundException() {
        Long invalidUserId = -1L;

        assertThatThrownBy(() -> itemRequestService.getUserRequests(invalidUserId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Пользователь с ID=" + invalidUserId + " не найден");
    }

    @Test
    @DisplayName("Получение собственных запросов: нет записей")
    void getUserRequests_whenNoRequests_shouldReturnEmptyList() {
        List<ItemRequestAnswerDto> result = itemRequestService.getUserRequests(userId);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Получение всех запросов с пагинацией")
    void getAllRequests_whenMultipleExist_shouldReturnPagedResult() {
        int limit = 2;
        int offset = 0;

        ItemRequest r1 = new ItemRequest();
        r1.setDescription("First");
        r1.setRequesterId(userId);
        r1.setCreated(now.minusHours(3));
        r1.setItems(List.of());

        ItemRequest r2 = new ItemRequest();
        r2.setDescription("Second");
        r2.setRequesterId(userId);
        r2.setCreated(now.minusHours(2));
        r2.setItems(List.of());

        ItemRequest r3 = new ItemRequest();
        r3.setDescription("Third");
        r3.setRequesterId(userId);
        r3.setCreated(now.minusHours(1));
        r3.setItems(List.of());

        itemRequestRepository.saveAll(List.of(r1, r2, r3));

        List<ItemRequestAnswerDto> result = itemRequestService.getAllRequests(limit, offset);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDescription()).isEqualTo("Third");
        assertThat(result.get(1).getDescription()).isEqualTo("Second");
    }

    @Test
    @DisplayName("Получение запроса по ID: запрос существует")
    void getRequest_whenExists_shouldReturnDto() {
        ItemRequest request = new ItemRequest();
        request.setDescription("Drill");
        request.setRequesterId(userId);
        request.setCreated(now);
        request.setItems(List.of());

        ItemRequest saved = itemRequestRepository.save(request);

        ItemRequestAnswerDto dto = itemRequestService.getRequest(saved.getId());

        assertThat(dto).isNotNull();
        assertThat(dto.getDescription()).isEqualTo("Drill");
        assertThat(dto.getItems()).isEmpty();
    }

    @Test
    @DisplayName("Получение запроса по ID: запрос не найден")
    void getRequest_whenNotExists_shouldThrowNotFoundException() {
        Long invalidRequestId = -1L;

        assertThatThrownBy(() -> itemRequestService.getRequest(invalidRequestId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Запрос с ID=" + invalidRequestId + " не найден");
    }
}