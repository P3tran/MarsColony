package com.pefthymiou.mars.notifications.infrastructure.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private String email;
    @Column
    private String unitTitle;
    @Column
    private Timestamp checkIn;
    @Column
    private Timestamp dueDate;

    public Notification() {
    }

    public Notification(String email, String unitTitle, Timestamp checkIn, Timestamp dueDate) {
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
