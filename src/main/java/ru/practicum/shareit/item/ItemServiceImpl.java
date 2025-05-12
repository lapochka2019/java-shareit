package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository repository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Item create(ItemDto itemDto, Integer owner) {
        checkOwner(owner);
        userRepository.getUser(owner);
        return repository.create(itemDto, owner);
    }

    @Override
    public Item update(ItemDto itemDto, Integer owner, int id) {
        checkOwner(owner);
        userRepository.getUser(owner);
        repository.getItem(id);
        return repository.update(itemDto, owner, id);
    }

    @Override
    public Item getItem(Integer id) {
        return repository.getItem(id);
    }

    @Override
    public List<Item> getItemsForOwner(Integer owner) {
        checkOwner(owner);
        return repository.getItemsForOwner(owner);
    }

    @Override
    public List<Item> itemSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        text = text.toLowerCase();
        return repository.searchItems(text);
    }

    public void checkOwner(Integer owner) {
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Заголовок X-Sharer-User-Id должен быть указан");
        }
    }
}
