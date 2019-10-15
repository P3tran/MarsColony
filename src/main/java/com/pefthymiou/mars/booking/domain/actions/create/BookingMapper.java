package com.pefthymiou.mars.booking.domain.actions.create;

import com.pefthymiou.mars.booking.domain.CreateBookingRequestDto;
import com.pefthymiou.mars.booking.infrastructure.db.Booking;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class BookingMapper {

    public Booking mapFrom(CreateBookingRequestDto request, String unitTimezone) {
        return new Booking(
                request.getUserId(),
                request.getUnitId(),
                timestampFrom(request.getCheckIn(), unitTimezone),
                timestampFrom(request.getCheckOut(), unitTimezone)
        );
    }

    public Timestamp timestampFrom(String checkIn, String timezone) {
        return new Timestamp(
                ZonedDateTime.parse(checkIn + "T00:00:00+00:00[" + ZoneId.of(timezone) + "]")
                        .toInstant()
                        .toEpochMilli()
        );
    }
}
