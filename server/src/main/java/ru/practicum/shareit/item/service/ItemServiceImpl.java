package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentsRepository commentsRepository;
    private final BookingMapper bookingMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto create(ItemCreateDto itemCreateDto, Long owner) {
        userService.checkUserExist(owner);

        Item item = itemMapper.dtoToItem(itemCreateDto, 0L, owner, null);

        if (itemCreateDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemCreateDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос не найден"));
            item.setRequest(request);
        }
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(ItemCreateDto itemCreateDto, Long owner, Long id) {
        userService.checkUserExist(owner);
        checkItemExist(id);
        Item existingItem = checkItemExist(id);
        if (!Objects.equals(existingItem.getOwner(), owner)) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }
        if (itemCreateDto.getName() != null) {
            existingItem.setName(itemCreateDto.getName());
        }
        if (itemCreateDto.getDescription() != null) {
            existingItem.setDescription(itemCreateDto.getDescription());
        }
        if (itemCreateDto.getAvailable() != null) {
            existingItem.setAvailable(itemCreateDto.getAvailable());
        }

        return itemMapper.toItemDto(itemRepository.save(existingItem));
    }

    @Override
    public ItemFullDto getItem(Long itemId, Long userId) {
        userService.checkUserExist(userId);
        log.info("Получаем вещь по itemId");
        Item item = checkItemExist(itemId);
        log.info("Получаем список комментариев вещи {}", item);
        List<CommentDto> commentList = commentsRepository.findAllByItemId((itemId)).stream()
                .map(commentMapper::toCommentDto)
                .toList();
        log.info("Получаем текущее время");
        LocalDateTime now = LocalDateTime.now();

        BookingCreationDto lasBooking = null;
        BookingCreationDto nextBooking = null;
        if (userId.equals(item.getOwner())) {
            log.info("Получаем последнее бронирование вещи");
            lasBooking = bookingRepository.getLastBooking(itemId, now)
                    .map(bookingMapper::toBookingCreationDto)
                    .orElse(null);
            log.info("Получаем следующее бронирование вещи");
            nextBooking = bookingRepository.getNextBooking(itemId, now)
                    .map(bookingMapper::toBookingCreationDto)
                    .orElse(null);
        }
        UserDto userDto = userService.getUser(item.getOwner());
        return itemMapper.toFullItem(item, lasBooking, nextBooking, commentList, userDto);
    }

    @Override
    public List<ItemDto> getItemsForOwner(Long owner) {
        return itemRepository.findByOwner(owner).stream()
                .map(itemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> itemSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text).stream()
                .map(itemMapper::toItemDto).toList();
    }

    @Override
    public Item checkItemExist(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + itemId + " не найден"));
    }

    @Override
    public CommentResponseDto addCommentToItem(Long userId, Long itemId, CommentDto commentDto) {
        log.info("Проверяем существование пользователя");
        User user = userMapper.toEntity(userService.getUser(userId));
        log.info("Проверяем существование вещи");
        Item item = checkItemExist(itemId);
        LocalDateTime now = LocalDateTime.now();
        if (bookingRepository.findAllByUserBookings(userId, itemId, now).isEmpty()) {
            throw new ValidationException("Пользователь не брал вещь в аренду");
        } else {
            Comment comment = commentMapper.toComment(1L, commentDto, item, user, now);
            return commentMapper.toCommentResponseDto(commentsRepository.save(comment));
        }
    }
}
