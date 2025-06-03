package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.CommentsRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final CommentMapper commentMapper;

    @Override
    public Item create(ItemDto itemDto, Long owner) {
        Item item = itemMapper.dtoToItem(itemDto, 0L, owner);
        userService.checkUserExist(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item update(ItemDto itemDto, Long owner, Long id) {
        userService.checkUserExist(owner);
        checkItemExist(id);
        Item existingItem = getItem(id);
        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequest() != null) {
            existingItem.setRequest(itemDto.getRequest());
        }
        return itemRepository.save(existingItem);
    }

    @Override
    public ItemFullDto getItem(Long itemId, Long userId) {
        log.info("Получаем вещь по itemId");
        Item item = getItem(itemId);
        log.info("Получаем список комментариев вещи {}", item);
        List<CommentDto> commentList = commentsRepository.findAllByItemId((itemId)).stream()
                .map(commentMapper::toCommentDto)
                .toList();
        log.info("Получаем текущее время");
        LocalDateTime now = LocalDateTime.now();

        BookingDto lasBooking = null;
        BookingDto nextBooking = null;
        if (userId.equals(item.getOwner())) {
            log.info("Получаем последнее бронирование вещи");
            lasBooking = bookingRepository.getLastBooking(itemId, now)
                    .map(bookingMapper::toBookingDto)
                    .orElse(null);
            log.info("Получаем следующее бронирование вещи");
            nextBooking = bookingRepository.getNextBooking(itemId, now)
                    .map(bookingMapper::toBookingDto)
                    .orElse(null);
        }
        return itemMapper.toNewItem(item, lasBooking, nextBooking, commentList);
    }

    @Override
    public List<Item> getItemsForOwner(Long owner) {
        return itemRepository.findByOwner(owner);
    }

    @Override
    public List<Item> itemSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.searchAvailableItems(text);
    }

    @Override
    public void checkItemExist(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Вещь с id " + id + " не найдена");
        }
    }

    public Item getItem(Long itemId){
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
    }

    @Override
    public CommentResponseDto addCommentToItem(Long userId, Long itemId, CommentDto commentDto) {
        log.info("Проверяем существование пользователя");
        User user = userService.getUser(userId);
        log.info("Проверяем существование вещи");
        Item item = getItem(itemId);
        LocalDateTime now = LocalDateTime.now();
        if (bookingRepository.findAllByUserBookings(userId, itemId, now).isEmpty()) {
            throw new ValidationException("Пользователь не брал вещь в аренду");
        } else {
            Comment comment = commentMapper.toComment(1L, commentDto, item, user, now);
            return commentMapper.toCommentResponseDto(commentsRepository.save(comment));
        }
    }
}
