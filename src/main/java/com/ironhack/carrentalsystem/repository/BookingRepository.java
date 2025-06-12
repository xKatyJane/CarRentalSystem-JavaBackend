package com.ironhack.carrentalsystem.repository;

import com.ironhack.carrentalsystem.model.Booking;
import com.ironhack.carrentalsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Query for the BookingServiceImpl, to check if car is already booked
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.car.id = :carId AND " +
            "(:startDateTime < b.endDateTime AND :endDateTime > b.startDateTime)")
    boolean existsByCarAndOverlappingDates(@Param("carId") Long carId,
                                           @Param("startDateTime") LocalDateTime startDateTime,
                                           @Param("endDateTime") LocalDateTime endDateTime);

    // Find all bookings of a customer
    List<Booking> findByCustomerId(Long customerId);

    // Query for the CarServiceImpl, to get IDs of booked cars
    @Query("SELECT b.car.id FROM Booking b WHERE " +
            "(:startDateTime < b.endDateTime AND :endDateTime > b.startDateTime)")
    List<Long> findBookedCarIdsBetween(@Param("startDateTime") LocalDateTime startDateTime,
                                       @Param("endDateTime") LocalDateTime endDateTime);

    // See bookings of each customer
    List<Booking> findByCustomer(User customer);

}
