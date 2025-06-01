package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "text", nullable = false)
    @NotBlank(message = "Отзыв не может быть пустым")
    String text;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    Item item;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    User author;
    @Column(name = "created")
    LocalDateTime created;

}