package com.pefthymiou.mars.dsl;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class BookingDsl {

    public static class ITBooking {

        private long id;
        private long userId;
        private long unitId;
        private String checkIn;
        private String checkOut;

        public ITBooking() {
        }

        public ITBooking(long id, long userId, long unitId, String checkIn, String checkOut) {
            this.id = id;
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

        public String getCheckIn() {
            return checkIn;
        }

        public String getCheckOut() {
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

    public static class ITBookingBuilder {

        private long id = 1;
        private long userId = 1;
        private long unitId = 1;
        private String checkIn = "2019-05-05";
        private String checkOut = "2019-05-15";

        public static ITBookingBuilder aBooking() {
            return new ITBookingBuilder();
        }

        public ITBookingBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public ITBookingBuilder withUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public ITBookingBuilder withUnitId(long unitId) {
            this.unitId = unitId;
            return this;
        }

        public ITBookingBuilder withCheckIn(String checkIn) {
            this.checkIn = checkIn;
            return this;
        }

        public ITBookingBuilder withCheckOut(String checkOut) {
            this.checkOut = checkOut;
            return this;
        }

        public ITBooking build() {
            return new ITBooking(
                    id,
                    userId,
                    unitId,
                    checkIn,
                    checkOut
            );
        }
    }
}
