package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository
public class ItemRepository {
    private final Map<Integer, Map<Integer, Item>> items = new HashMap<>();
    private int id = 1;

    public Item create(ItemDto itemDto, int owner) {
        //Заполнили все поля вещи
        Item item = ItemDtoMapper.toItemDto(itemDto, id, owner);
        Map<Integer, Item> itemsMap;
        //Проверка на "существование" пользователя
        if (items.containsKey(owner)) {
            itemsMap = items.get(owner);
        } else {
            itemsMap = new HashMap<>();
        }
        //Создали/подтянули мапу
        //Добавили в мапу новую вещь
        itemsMap.put(id, item);
        //Обновили мапу пользователя в общей "БД"
        items.put(owner, itemsMap);
        id++;
        log.info("Вещь успешно создана: {}", item);
        return item;
    }

    public Item update(ItemDto itemDto, int owner, int id) {
        //Получили мапу пользователя
        Map<Integer, Item> itemsMap = items.get(owner);
        //Получили старые данные о вещи с id
        Item oldItem = itemsMap.get(id);

        //Обновляем данные, если они есть
        if (itemDto.getName() != null && !itemDto.getName().isEmpty() && !itemDto.getName().isBlank()) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty() && !itemDto.getDescription().isBlank()) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequestId() != null) {
            oldItem.setRequest(itemDto.getRequestId());
        }
        //Добавляем вещь в список пользователя
        itemsMap.put(id, oldItem);
        //Обновляем "данные" пользователя
        items.put(owner, itemsMap);
        log.info("Вещь успешно обновлена: {}", itemDto);
        return oldItem;
    }

    public Item getItem(int id) {
        return items.values().stream()
                .map(map -> map.get(id))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Вещь с ID " + id + " не найдена"));

    }

    public List<Item> getItemsForOwner(Integer owner) {
        try {
            return items.get(owner).values().stream().toList();
        } catch (NullPointerException e) {
            throw new NotFoundException("Вещи для пользователя с ID " + id + " не найдены");
        }
    }

    public List<Item> searchItems(String text) {
        return items.values().stream()
                .flatMap(userItems -> userItems.values().stream())
                .filter(Objects::nonNull)
                .filter(item -> (item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text)) &&
                        item.getAvailable())
                .toList();

    }
}
