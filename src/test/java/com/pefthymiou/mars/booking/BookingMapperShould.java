package com.pefthymiou.mars.booking;

import com.pefthymiou.mars.booking.domain.CreateBookingRequestDto;
import com.pefthymiou.mars.booking.domain.actions.create.BookingMapper;
import com.pefthymiou.mars.booking.infrastructure.db.Booking;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

import static com.pefthymiou.mars.base.builders.BookingBuilder.aBooking;

public class BookingMapperShould {

    private BookingMapper bookingMapper = new BookingMapper();

    private CreateBookingRequestDto request = new CreateBookingRequestDto(
            2,
            3,
            "2019-05-05",
            "2019-05-15"
    );
    private Booking expectedBooking = aBooking()
            .withUserId(2)
            .withUnitId(3)
            .withCheckIn(new Timestamp(ZonedDateTime.parse("2019-05-05T00:00:00+00:00[Europe/London]").toInstant().toEpochMilli()))
            .withCheckOut(new Timestamp(ZonedDateTime.parse("2019-05-15T00:00:00+00:00[Europe/London]").toInstant().toEpochMilli()))
            .build();

    @Test
    public void mapACreateBookingRequestDtoToBooking() {
        Booking actualBooking = bookingMapper.mapFrom(request, "Europe/London");

        Assertions.assertThat(actualBooking).isEqualToComparingFieldByField(expectedBooking);
    }
}