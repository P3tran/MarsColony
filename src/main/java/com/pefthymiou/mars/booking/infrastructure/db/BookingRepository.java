package com.pefthymiou.mars.booking.infrastructure.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {

    @Query("Select b from Booking b " +
            "where :checkIn >= b.checkIn and :checkIn <= b.checkOut " +
            "or :checkOut >= b.checkIn and :checkOut <= b.checkOut")
    Iterable<Booking> bookingsWithin(@Param("checkIn") Timestamp checkIn, @Param("checkOut") Timestamp checkOut);

    boolean existsByUnitId(Long id);
}
