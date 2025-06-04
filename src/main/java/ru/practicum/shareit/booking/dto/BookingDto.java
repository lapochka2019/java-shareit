package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    @NotNull
    Long itemId;
    @NotNull(message = "Время начала бронирования должно быть заполнено")
    LocalDateTime start;
    @NotNull(message = "Время окончания бронирования должно быть заполнено")
    LocalDateTime end;
}