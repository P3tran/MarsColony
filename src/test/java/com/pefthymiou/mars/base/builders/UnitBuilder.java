package com.pefthymiou.mars.base.builders;

import com.pefthymiou.mars.unit.infrastructure.db.Unit;

public class UnitBuilder {

    private long id = 1;
    private String title = "a title";
    private String region = "a region";
    private String description = "a description";
    private String cancellationPolicy = "a cancellation policy";
    private int price = 500;
    private int rating = 4;
    private String imageUrl = "https://example.com/img/unit.png";
    private String timezone = "UTC";

    public static UnitBuilder aUnit() {
        return new UnitBuilder();
    }

    public UnitBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public UnitBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public UnitBuilder withRegion(String region) {
        this.region = region;
        return this;
    }

    public UnitBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public UnitBuilder withCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
        return this;
    }

    public UnitBuilder withPrice(int price) {
        this.price = price;
        return this;
    }

    public UnitBuilder withRating(int rating) {
        this.rating = rating;
        return this;
    }

    public UnitBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UnitBuilder withTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public Unit build() {
        return new Unit(
                id,
                title,
                region,
                description,
                cancellationPolicy,
                price,
                rating,
                imageUrl,
                timezone
        );
    }
}
