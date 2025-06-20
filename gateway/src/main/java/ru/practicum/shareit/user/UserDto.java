package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.Marker;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя пользователя должно быть заполнено", groups = {Marker.OnCreate.class})
    private String name;
    @NotBlank(message = "Поле email должно быть заполнено", groups = {Marker.OnCreate.class})
    @Email(message = "Это не похоже на email")
    private String email;
}
