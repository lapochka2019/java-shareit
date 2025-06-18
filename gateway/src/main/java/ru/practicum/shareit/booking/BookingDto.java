package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
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
    @NotNull(message = "Поле itemId должно быть заполнено")
    Long itemId;
    @NotNull(message = "Время начала бронирования должно быть заполнено")
    @FutureOrPresent(message = "Время начала бронирования должно быть в настоящем или будущем")
    LocalDateTime start;
    @NotNull(message = "Время окончания бронирования должно быть заполнено")
    @Future (message = "Время окончания бронирования должно быть в будущем")
    LocalDateTime end;
}