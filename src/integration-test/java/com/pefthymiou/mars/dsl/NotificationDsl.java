package com.pefthymiou.mars.dsl;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class NotificationDsl {

    public static class ITNotification {

        private long id;
        private String email;
        private String unitTitle;
        private Timestamp checkIn;
        private Timestamp dueDate;

        public ITNotification(long id, String email, String unitTitle, Timestamp checkIn, Timestamp dueDate) {
            this.id = id;
            this.email = email;
            this.unitTitle = unitTitle;
            this.checkIn = checkIn;
            this.dueDate = dueDate;
        }

        public long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getUnitTitle() {
            return unitTitle;
        }

        public Timestamp getCheckIn() {
            return checkIn;
        }

        public Timestamp getDueDate() {
            return dueDate;
        }

        @Override
        public boolean equals(Object other) {
            return reflectionEquals(this, other);
        }

        @Override
        public int hashCode() {
            return reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return reflectionToString(this, MULTI_LINE_STYLE);
        }
    }

    public static class ITNotificationBuilder {

        private long id = 1;
        private String email = "someone@example.com";
        private String unitTitle = "a title";
        private Timestamp checkIn = new Timestamp(ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());
        private Timestamp dueDate = new Timestamp(ZonedDateTime.of(2019, 6, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli());

        public static ITNotificationBuilder aNotification() {
            return new ITNotificationBuilder();
        }

        public ITNotificationBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ITNotificationBuilder withUnitTitle(String unitTitle) {
            this.unitTitle = unitTitle;
            return this;
        }

        public ITNotificationBuilder withCheckIn(Timestamp checkIn) {
            this.checkIn = checkIn;
            return this;
        }

        public ITNotificationBuilder withDueDate(Timestamp dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public ITNotification build() {
            return new ITNotification(
                    id,
                    email,
                    unitTitle,
                    checkIn,
                    dueDate
            );
        }

    }
}
