package com.pefthymiou.mars.booking;

import com.pefthymiou.mars.booking.domain.CreateBookingRequestDto;
import com.pefthymiou.mars.booking.domain.actions.create.BookingValidator;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.RequestDto;
import org.junit.Test;

public class BookingValidatorShould {

    private BookingValidator bookingValidator = new BookingValidator();

    @Test
    public void notThrowInvalidRequestExceptionWhenRequestIsValid() {
        bookingValidator.validate(new CreateBookingRequestDto(
                2,
                3,
                "2020-05-05",
                "2020-05-15"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRequestIsNull() {
        bookingValidator.validate(null);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRequestIsNotACreateBookingRequestDto() {
        bookingValidator.validate(new UnexpectedRequest());
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCheckInIsNull() {
        bookingValidator.validate(new CreateBookingRequestDto(
                2,
                3,
                null,
                "2020-05-15"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCheckInIsNotProperlyFormatted() {
        bookingValidator.validate(new CreateBookingRequestDto(
                2,
                3,
                "05-05-2020",
                "2020-05-15"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCheckOutIsNull() {
        bookingValidator.validate(new CreateBookingRequestDto(
                        2,
                        3,
                        "2020-05-05",
                        null
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCheckOutIsNotProperlyFormatted() {
        bookingValidator.validate(new CreateBookingRequestDto(
                2,
                3,
                "2020-05-05",
                "15-05-2020"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCheckInDateIsInThePast() {
        bookingValidator.validate(new CreateBookingRequestDto(
                2,
                3,
                "2018-05-05",
                "2020-05-15"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCheckOutDateIsBeforeCheckInDate() {
        bookingValidator.validate(new CreateBookingRequestDto(
                2,
                3,
                "2020-05-05",
                "2020-05-04"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCheckInAndCheckOutAreTheSameDay() {
        bookingValidator.validate(new CreateBookingRequestDto(
                2,
                3,
                "2020-05-05",
                "2020-05-05"
                )
        );
    }

    private class UnexpectedRequest implements RequestDto {
    }
}