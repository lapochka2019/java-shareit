package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @NotBlank(message = "description должен быть заполнен")
    @Size(max = 512, message = "Длина описания не должна превышать 512 символов")
    private String description;
}
