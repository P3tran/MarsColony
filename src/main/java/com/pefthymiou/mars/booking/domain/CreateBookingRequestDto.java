package com.pefthymiou.mars.booking.domain;

import com.pefthymiou.mars.unit.domain.RequestDto;

public class CreateBookingRequestDto implements RequestDto {

    private long userId;
    private long unitId;
    private String checkIn;
    private String checkOut;

    public CreateBookingRequestDto() {
    }

    public CreateBookingRequestDto(long userId, long unitId, String checkIn, String checkOut) {
        this.userId = userId;
        this.unitId = unitId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public long getUserId() {
        return userId;
    }

    public long getUnitId() {
        return unitId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }
}
