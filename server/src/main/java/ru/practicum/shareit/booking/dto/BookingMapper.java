package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "booker", source = "user")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "start", source = "dto.start")
    @Mapping(target = "end", source = "dto.end")
    Booking toBooking(Long id, BookingDto dto, Item item, User user, BookingStatus status);

    @Mapping(target = "itemId", source = "booking.item.id")
    BookingDto toBookingDto(Booking booking);
}