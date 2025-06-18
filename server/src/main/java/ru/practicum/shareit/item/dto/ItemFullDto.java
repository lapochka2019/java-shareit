package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemFullDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingCreationDto lastBooking;
    private BookingCreationDto nextBooking;
    private List<CommentDto> comments;
    private Long request;
    UserDto owner;
}
