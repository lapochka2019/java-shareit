package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
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
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    UserService userService;
    BookingRepository bookingRepository;
    CommentsRepository commentsRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, BookingRepository bookingRepository, UserService userService, CommentsRepository commentsRepository) {
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.commentsRepository = commentsRepository;
    }

    @Override
    public Item create(ItemDto itemDto, Long owner) {
        Item item = ItemMapper.toItem(itemDto, 0L, owner);
        userService.checkUserExist(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item update(ItemDto itemDto, Long owner, Long id) {
        userService.checkUserExist(owner);
        checkItemExist(id);
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + id + " не найдена"));
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
    public NewItem getNewItem(Long itemId, Long userId) {
        log.info("Получаем вещь по itemId");
        Item item = getItem(itemId);
        log.info("Получаем список комментариев вещи {}", item);
        List<CommentDto> commentList = commentsRepository.findAllByItemId((itemId)).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
        log.info("Получаем текущее время");
        LocalDateTime now = LocalDateTime.now();

        BookingDto lasBooking = null;
        BookingDto nextBooking = null;
        if (userId.equals(item.getOwner())) {
            log.info("Получаем последнее бронирование вещи");
            lasBooking = bookingRepository.getLastBooking(itemId, now)
                    .map(BookingMapper::toBookingDto)
                    .orElse(null);
            log.info("Получаем следующее бронирование вещи");
            nextBooking = bookingRepository.getNextBooking(itemId, now)
                    .map(BookingMapper::toBookingDto)
                    .orElse(null);
        }
        return ItemMapper.toNewItem(item, lasBooking, nextBooking, commentList);
    }

    @Override
    public Item getItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException("Вещь с id " + id + " не найдена");
        } else {
            return optionalItem.get();
        }
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
            Comment comment = CommentMapper.toComment(1L, commentDto, item, user, now);
            return CommentMapper.toCommentResponseDto(commentsRepository.save(comment));
        }
    }
}
