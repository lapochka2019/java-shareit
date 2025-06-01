package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Время начала бронирования должно быть заполнено")
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    @NotNull(message = "Время окончания бронирования должно быть заполнено")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    @ToString.Exclude
    User booker;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Укажите статус брони")
    private BookingStatus status;
}