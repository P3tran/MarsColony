package com.pefthymiou.mars.dsl;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class UnitDsl {

    public static class ITUnit {

        private long id;
        private String title;
        private String region;
        private String description;
        private String cancellationPolicy;
        private int price;
        private int rating;
        private String imageUrl;
        private String timezone;

        public ITUnit() {
        }

        public ITUnit(
                long id,
                String title,
                String region,
                String description,
                String cancellationPolicy,
                int price,
                int rating,
                String imageUrl,
                String timezone
        ) {
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

    public static class ITUnitBuilder {

        private long id = 1;
        private String title = "a title";
        private String region = "a region";
        private String description = "a description";
        private String cancellationPolicy = "a cancellation policy";
        private int price = 500;
        private int rating = 4;
        private String imageUrl = "https://example.com/img/unit.png";
        private String timezone = "UTC";

        public static ITUnitBuilder aUnit() {
            return new ITUnitBuilder();
        }

        public ITUnitBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public ITUnitBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public ITUnitBuilder withRegion(String region) {
            this.region = region;
            return this;
        }

        public ITUnitBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ITUnitBuilder withCancellationPolicy(String cancellationPolicy) {
            this.cancellationPolicy = cancellationPolicy;
            return this;
        }

        public ITUnitBuilder withPrice(int price) {
            this.price = price;
            return this;
        }

        public ITUnitBuilder withRating(int rating) {
            this.rating = rating;
            return this;
        }

        public ITUnitBuilder withImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ITUnitBuilder withTimezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public ITUnit build() {
            return new ITUnit(
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
}
