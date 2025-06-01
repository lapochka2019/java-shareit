package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByBookerId(@Param("bookerId") Long bookerId);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND :time BETWEEN start_date AND end_date ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findCurrentBookingByBookerId(@Param("bookerId") Long bookerId,
                                               @Param("time") LocalDateTime time);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId AND end_date < :time " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findPastBookingByBookerId(@Param("bookerId") Long bookerId,
                                            @Param("time") LocalDateTime time);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId AND start_date > :time " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findFutureBookingByBookerId(@Param("bookerId") Long bookerId,
                                              @Param("time") LocalDateTime time);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND status = 'WAITING' AND start_date > :time ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findWaitingBookingByBookerId(@Param("bookerId") Long bookerId,
                                               @Param("time") LocalDateTime time);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId AND status = 'REJECTED' " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findRejectBookingByBookerId(@Param("bookerId") Long bookerId);

    @Query(value = "SELECT b.* FROM bookings b " +
            "JOIN items i ON i.id = b.item_id WHERE i.owner_id = :ownerId " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingsByOwnerId(@Param("ownerId") Long ownerId);

    @Query(value = "SELECT b.* FROM bookings b " +
            "JOIN items i ON i.id = b.item_id WHERE i.owner_id = :ownerId " +
            "AND :currentTime BETWEEN b.start_date AND b.end_date ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllCurrentBookingsByOwnerId(@Param("ownerId") Long ownerId,
                                                  @Param("currentTime") LocalDateTime currentTime);

    @Query(value = "SELECT b.* FROM bookings b " +
            "JOIN items i ON i.id = b.item_id WHERE i.owner_id = :ownerId " +
            "AND b.end_date < :currentTime ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllPastBookingsByOwnerId(@Param("ownerId") Long ownerId,
                                               @Param("currentTime") LocalDateTime currentTime);

    @Query(value = "SELECT b.* FROM bookings b " +
            "JOIN items i ON i.id = b.item_id WHERE i.owner_id = :ownerId " +
            "AND b.start_date > :currentTime ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllFutureBookingsByOwnerId(@Param("ownerId") Long ownerId,
                                                 @Param("currentTime") LocalDateTime currentTime);

    @Query(value = "SELECT b.* FROM bookings b " +
            "JOIN items i ON i.id = b.item_id WHERE i.owner_id = :ownerId " +
            "AND b.status = 'WAITING' AND b.start_date > :currentTime ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllWaitingBookingsByOwnerId(@Param("ownerId") Long ownerId,
                                                  @Param("currentTime") LocalDateTime currentTime);

    @Query(value = "SELECT b.* FROM bookings b " +
            "JOIN items i ON i.id = b.item_id WHERE i.owner_id = :ownerId " +
            "AND b.status = 'REJECTED' ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllRejectedBookingsByOwnerId(@Param("ownerId") Long ownerId);

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