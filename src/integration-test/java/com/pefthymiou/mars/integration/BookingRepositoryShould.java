package com.pefthymiou.mars.integration;

import com.pefthymiou.mars.booking.infrastructure.db.Booking;
import com.pefthymiou.mars.booking.infrastructure.db.BookingRepository;
import com.pefthymiou.mars.dsl.BookingDsl;
import com.pefthymiou.mars.dsl.MarsDsl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Iterator;

import static com.pefthymiou.mars.dsl.BookingDsl.ITBookingBuilder.aBooking;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookingRepositoryShould {

    private static final long USER_ID = 1;
    private static final BookingDsl.ITBooking BOOKING = aBooking().withUserId(USER_ID).withCheckIn("2020-05-15").withCheckOut("2020-05-20").build();

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private MarsDsl marsDsl;

    @Before
    public void setup() {
        marsDsl.create(BOOKING);
    }

    @Test
    public void fetchBookingsWithinDatesWhenThereIsABookingAtCheckOutDate() {
        Iterable<Booking> actualBookings = bookingRepository.bookingsWithin(timestampFor("2020-05-10"), timestampFor("2020-05-15"));

        Iterator<Booking> iterator = actualBookings.iterator();
        assertThat(iterator.next().getUserId()).isEqualTo(BOOKING.getUserId());
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void fetchBookingsWithinDatesWhenThereIsABookingAtCheckInDate() {
        Iterable<Booking> actualBookings = bookingRepository.bookingsWithin(timestampFor("2020-05-20"), timestampFor("2020-05-25"));

        Iterator<Booking> iterator = actualBookings.iterator();
        assertThat(iterator.next().getUserId()).isEqualTo(BOOKING.getUserId());
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void fetchBookingsWithinDatesWhenThereIsABookingAtCheckInAndCheckOutDate() {
        Iterable<Booking> actualBookings = bookingRepository.bookingsWithin(timestampFor("2020-05-16"), timestampFor("2020-05-18"));

        Iterator<Booking> iterator = actualBookings.iterator();
        assertThat(iterator.next().getUserId()).isEqualTo(BOOKING.getUserId());
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void fetchNoBookingsWithinDatesWhenThereIsANoBookingAtCheckInOrCheckOutDate() {
        Iterable<Booking> actualBookings = bookingRepository.bookingsWithin(timestampFor("2020-05-25"), timestampFor("2020-05-30"));

        Iterator<Booking> iterator = actualBookings.iterator();
        assertThat(iterator.hasNext()).isFalse();
    }

    private Timestamp timestampFor(String date) {
        return new Timestamp(
                ZonedDateTime.parse(date + "T00:00:00+00:00[Europe/London]")
                        .toInstant()
                        .toEpochMilli()
        );
    }
}
