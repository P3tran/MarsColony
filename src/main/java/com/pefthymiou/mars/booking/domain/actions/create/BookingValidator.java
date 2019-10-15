package com.pefthymiou.mars.booking.domain.actions.create;

import com.pefthymiou.mars.booking.domain.CreateBookingRequestDto;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.RequestDto;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class BookingValidator {

    private static final Date NOW = new Date(System.currentTimeMillis());
    private static final String INVALID_REQUEST = "Invalid request";
    private static final String CHECK_IN_NULL = "Check-in date may not be null";
    private static final String CHECK_OUT_NULL = "Check-out date may not be null";
    private static final String CHECK_IN_WRONG_FORMAT = "Check-in must be of format yyyy-MM-dd";
    private static final String CHECK_OUT_WRONG_FORMAT = "Check-out must be of format yyyy-MM-dd";
    private static final String CHECK_IN_PAST = "Check-in cannot be in the past";
    private static final String CHECK_OUT_BEFORE_CHECK_IN = "Check-out cannot be before check-in";
    private static final String MIN_BOOKING_DURATION = "Minimum booking duration is 1 day";

    public void validate(RequestDto request) {
        if (request == null)
            throw new InvalidRequestException(INVALID_REQUEST);

        CreateBookingRequestDto createBookingRequestDto = from(request);

        if (createBookingRequestDto.getCheckIn() == null)
            throw new InvalidRequestException(CHECK_IN_NULL);

        if (createBookingRequestDto.getCheckOut() == null)
            throw new InvalidRequestException(CHECK_OUT_NULL);

        Date checkInDate;
        try {
            checkInDate = Date.valueOf(createBookingRequestDto.getCheckIn());
            if (checkInDate.before(NOW))
                throw new InvalidRequestException(CHECK_IN_PAST);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(CHECK_IN_WRONG_FORMAT);
        }

        try {
            Date checkOutDate = Date.valueOf(createBookingRequestDto.getCheckOut());
            if (checkOutDate.before(checkInDate))
                throw new InvalidRequestException(CHECK_OUT_BEFORE_CHECK_IN);
            if (checkInDate.equals(checkOutDate))
                throw new InvalidRequestException(MIN_BOOKING_DURATION);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(CHECK_OUT_WRONG_FORMAT);
        }
    }

    private CreateBookingRequestDto from(RequestDto request) {
        try {
            return (CreateBookingRequestDto) request;
        } catch (ClassCastException e) {
            throw new InvalidRequestException(INVALID_REQUEST);
        }
    }
}
