package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.Marker;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    @NotBlank(message = "name должен быть заполнен", groups = Marker.OnCreate.class)
    @Size(max = 255, message = "Длина Имени не должна превышать 255 символов")
    private String name;
    @NotBlank(message = "description должен быть заполнен", groups = Marker.OnCreate.class)
    @Size(max = 512, message = "Длина описания не должна превышать 512 символов")
    private String description;
    @NotNull(message = "available должен быть заполнен", groups = Marker.OnCreate.class)
    private Boolean available;
    private Long requestId;
}
