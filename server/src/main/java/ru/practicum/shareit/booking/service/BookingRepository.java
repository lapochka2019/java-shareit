package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    //ALL state by bookerId
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    //CURRENT state by bookingId
    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime end, LocalDateTime start);

    //PAST state by bookingId
    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    //FUTURE state by bookingId
    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    //WAITING state by bookingId
    List<Booking> findByBookerIdAndStatusAndStartAfterOrderByStartDesc(Long bookerId, BookingStatus status, LocalDateTime start);

    //REJECT state by bookingId
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    //ALL state by ownerId
    List<Booking> findByItem_OwnerOrderByStartDesc(Long ownerId);

    //CURRENT state by ownerId
    List<Booking> findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime end, LocalDateTime start);

    //PAST state by ownerId
    List<Booking> findByItem_OwnerAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime end);

    //FUTURE state by ownerId
    List<Booking> findByItem_OwnerAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    //WAITING state by ownerId
    List<Booking> findByItem_OwnerAndStatusAndStartAfterOrderByStartDesc(Long ownerId, BookingStatus status, LocalDateTime start);

    //REJECT state by ownerId
    List<Booking> findByItem_OwnerAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :userId AND item_id = :itemId " +
            "AND status = 'APPROVED' AND end_date < :now", nativeQuery = true)
    List<Booking> findAllByUserBookings(@Param("userId") Long userId,
                                        @Param("itemId") Long itemId,
                                        @Param("now") LocalDateTime now);

    @Query(value = "SELECT * FROM bookings WHERE item_id = :itemId AND start_date < :now " +
            "AND status = 'APPROVED' ORDER BY start_date DESC LIMIT 1", nativeQuery = true)
    Optional<Booking> getLastBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query(value = "SELECT * FROM bookings WHERE item_id = :itemId AND start_date > :now " +
            "AND status = 'APPROVED' ORDER BY start_date ASC LIMIT 1", nativeQuery = true)
    Optional<Booking> getNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);
}