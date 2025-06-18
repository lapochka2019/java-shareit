package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    private final ItemMapper mapper = ItemMapper.INSTANCE;

    private Item item;
    private BookingCreationDto lastBooking;
    private BookingCreationDto nextBooking;
    private List<CommentDto> comments;
    private UserDto owner;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);
        item.setName("Drill");
        item.setDescription("Powerful drill");
        item.setAvailable(true);
        item.setOwner(100L);

        request = new ItemRequest(5L, "Request", 1L, LocalDateTime.now(), null);
        item.setRequest(request);

        lastBooking = new BookingCreationDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(3));

        nextBooking = new BookingCreationDto(1L, LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(7));

        CommentDto comment = new CommentDto();
        comment.setText("Good!");
        comments = List.of(comment);

        owner = new UserDto();
        owner.setId(100L);
        owner.setName("Alice");
        owner.setEmail("alice@example.com");
    }

    @Test
    @DisplayName("dtoToItem: преобразование CreateDto в Item")
    void dtoToItem_ShouldMapCorrectly() {
        ItemCreateDto createDto = new ItemCreateDto();
        createDto.setName("Телефон");
        createDto.setDescription("Мобильный телефон");
        createDto.setAvailable(false);
        createDto.setRequestId(request.getId());

        Item result = mapper.dtoToItem(createDto, 2L, 200L, request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Телефон");
        assertThat(result.getDescription()).isEqualTo("Мобильный телефон");
        assertThat(result.getAvailable()).isFalse();
        assertThat(result.getOwner()).isEqualTo(200L);
        assertThat(result.getRequest().getId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("toFullItem: преобразование Item в FullDto")
    void toFullItem_ShouldMapAllFieldsIncludingBookingsAndComments() {
        ItemFullDto fullDto = mapper.toFullItem(item, lastBooking, nextBooking, comments, owner);

        assertThat(fullDto).isNotNull();
        assertThat(fullDto.getId()).isEqualTo(1L);
        assertThat(fullDto.getName()).isEqualTo("Drill");
        assertThat(fullDto.getDescription()).isEqualTo("Powerful drill");
        assertThat(fullDto.getAvailable()).isTrue();
        assertThat(fullDto.getOwner().getName()).isEqualTo("Alice");
        assertThat(fullDto.getRequest()).isEqualTo(5L);
        assertThat(fullDto.getLastBooking()).isEqualTo(lastBooking);
        assertThat(fullDto.getNextBooking()).isEqualTo(nextBooking);
        assertThat(fullDto.getComments()).hasSize(1);
    }

    @Test
    @DisplayName("toItemDtoForRequest: преобразование Item в Dto для запроса")
    void toItemDtoForRequest_ShouldMapBasicFields() {
        ItemDtoForRequest dto = mapper.toItemDtoForRequest(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getOwner()).isEqualTo(100L);
    }

    @Test
    @DisplayName("toItemDto: преобразование Item в базовый Dto")
    void toItemDto_ShouldMapWithRequestId() {
        ItemDto dto = mapper.toItemDto(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getDescription()).isEqualTo("Powerful drill");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getOwner()).isEqualTo(100L);
        assertThat(dto.getRequestId()).isEqualTo(5L);
    }
}