package com.pefthymiou.mars.dsl;

import com.eclipsesource.json.JsonObject;

import java.util.Map;

public class Parser {

    public static String jsonWith(ITCredentials credentials) {
        return new JsonObject()
                .add("username", credentials.getUsername())
                .add("password", credentials.getPassword())
                .toString();
    }

    public static String jsonWith(UnitDsl.ITUnit unit) {
        return new JsonObject()
                .add("title", unit.getTitle())
                .add("region", unit.getRegion())
                .add("description", unit.getDescription())
                .add("cancellationPolicy", unit.getCancellationPolicy())
                .add("price", unit.getPrice())
                .add("rating", unit.getRating())
                .add("imageUrl", unit.getImageUrl())
                .add("timezone", unit.getTimezone())
                .toString();
    }

    public static String jsonWith(Map<String, Object> fields) {
        JsonObject json = new JsonObject();

        fields.forEach((key, value) ->
                json.add(key, value.toString())
        );

        return json.toString();
    }

    public static String jsonWith(BookingDsl.ITBooking booking) {
        return new JsonObject()
                .add("userId", booking.getUserId())
                .add("unitId", booking.getUnitId())
                .add("checkIn", booking.getCheckIn())
                .add("checkOut", booking.getCheckOut())
                .toString();
    }
}
