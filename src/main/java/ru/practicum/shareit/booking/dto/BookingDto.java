package ru.practicum.shareit.booking.dto;

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
    @NotNull
    Long itemId;
    @NotNull
    @FutureOrPresent (message = "Дата бронирования должна быть в настоящем или будущем")
    LocalDateTime start;
    @NotNull
    @Future (message = "Дата бронирования может быть только в будущем")
    LocalDateTime end;
}