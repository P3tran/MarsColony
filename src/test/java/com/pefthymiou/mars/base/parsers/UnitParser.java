package com.pefthymiou.mars.base.parsers;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import java.util.List;

public class UnitParser {

    public static JsonArray jsonWith(List<Unit> units) {
        JsonArray json = new JsonArray();
        units.forEach(unit -> json.add(jsonWith(unit)));
        return json;
    }

    private static JsonObject jsonWith(Unit unit) {
        return new JsonObject()
                .add("id", unit.getId())
                .add("title", unit.getTitle())
                .add("region", unit.getRegion())
                .add("description", unit.getDescription())
                .add("cancellationPolicy", unit.getCancellationPolicy())
                .add("price", unit.getPrice())
                .add("rating", unit.getRating())
                .add("imageUrl", unit.getImageUrl());
    }
}
