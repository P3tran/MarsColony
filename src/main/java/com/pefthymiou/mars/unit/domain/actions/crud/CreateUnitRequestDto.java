package com.pefthymiou.mars.unit.domain.actions.crud;

import com.pefthymiou.mars.unit.domain.RequestDto;

public class CreateUnitRequestDto implements RequestDto {

    private String title;
    private String region;
    private String description;
    private String cancellationPolicy;
    private int price;
    private int rating;
    private String imageUrl;
    private String timezone;

    public CreateUnitRequestDto() {
    }

    public CreateUnitRequestDto(String title, String region, String description, String cancellationPolicy, int price, int rating, String imageUrl, String timezone) {
        this.title = title;
        this.region = region;
        this.description = description;
        this.cancellationPolicy = cancellationPolicy;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.timezone = timezone;
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
}
