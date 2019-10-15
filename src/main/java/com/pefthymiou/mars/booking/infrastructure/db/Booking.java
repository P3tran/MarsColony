package com.pefthymiou.mars.booking.infrastructure.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

@Entity
public class Booking {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private long userId;
    @Column
    private long unitId;
    @Column
    private Timestamp checkIn;
    @Column
    private Timestamp checkOut;

    public Booking() {
    }

    public Booking(long userId, long unitId, Timestamp checkIn, Timestamp checkOut) {
        this.userId = userId;
        this.unitId = unitId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getUnitId() {
        return unitId;
    }

    public Timestamp getCheckIn() {
        return checkIn;
    }

    public Timestamp getCheckOut() {
        return checkOut;
    }

    @Override
    public boolean equals(Object other) {
        return reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }
}
