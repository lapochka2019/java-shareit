package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Column(name = "description", nullable = false)
    @Size(max = 200, message = "Длина описания не должна превышать 200 символов")
    private String description;
    @Column(name = "is_available", nullable = false)
    @NotNull
    private Boolean available;
    @Column(name = "user_id", nullable = false)
    @NotNull(message = "Вещь должна иметь владельца")
    private Long owner;
    @Column(name = "request_id")
    private Long request;
}
