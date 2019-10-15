package com.pefthymiou.mars.base.builders;

import com.pefthymiou.mars.booking.infrastructure.db.Booking;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BookingBuilder {

    private long id = 1;
    private long userId = 2;
    private long unitId = 3;
    private Timestamp checkIn = new Timestamp(ZonedDateTime.of(2019, 5, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());
    private Timestamp checkOut = new Timestamp(ZonedDateTime.of(2019, 5, 15, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());

    public static BookingBuilder aBooking() {
        return new BookingBuilder();
    }

    public BookingBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public BookingBuilder withUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public BookingBuilder withUnitId(long unitId) {
        this.unitId = unitId;
        return this;
    }

    public BookingBuilder withCheckIn(Timestamp checkIn) {
        this.checkIn = checkIn;
        return this;
    }

    public BookingBuilder withCheckOut(Timestamp checkOut) {
        this.checkOut = checkOut;
        return this;
    }

    public Booking build() {
        return new Booking(
                userId,
                unitId,
                checkIn,
                checkOut
        );
    }
}
