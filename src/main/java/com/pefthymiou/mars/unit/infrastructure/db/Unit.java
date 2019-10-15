package com.pefthymiou.mars.unit.infrastructure.db;

import com.pefthymiou.mars.unit.domain.Resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Unit implements Resource {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private String title;
    @Column
    private String region;
    @Column
    private String description;
    @Column
    private String cancellationPolicy;
    @Column
    private int price;
    @Column
    private int rating;
    @Column
    private String imageUrl;
    @Column
    private String timezone;

    public Unit() {
    }

    public Unit(String title, String region, String description, String cancellationPolicy, int price, int rating, String imageUrl, String timezone) {
        this.title = title;
        this.region = region;
        this.description = description;
        this.cancellationPolicy = cancellationPolicy;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.timezone = timezone;
    }

    public Unit(long id, String title, String region, String description, String cancellationPolicy, int price, int rating, String imageUrl, String timezone) {
        this.id = id;
        this.title = title;
        this.region = region;
        this.description = description;
        this.cancellationPolicy = cancellationPolicy;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.timezone = timezone;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRegion() {
        return region;
    }

    public String getDescription() {
        return description;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public int getPrice() {
        return price;
    }

    public int getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTimezone() {
        return timezone;
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
