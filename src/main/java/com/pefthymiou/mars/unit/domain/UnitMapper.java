package com.pefthymiou.mars.unit.domain;

import com.pefthymiou.mars.unit.domain.actions.crud.CreateUnitRequestDto;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UnitMapper {

    private static final String REGION = "region";
    private static final String DESCRIPTION = "description";
    private static final String CANCELLATION_POLICY = "cancellationPolicy";
    private static final String PRICE = "price";
    private static final String RATING = "rating";
    private static final String IMAGE_URL = "imageUrl";

    public Unit mapFrom(CreateUnitRequestDto request) {
        return new Unit(
                request.getTitle(),
                request.getRegion(),
                request.getDescription(),
                request.getCancellationPolicy(),
                request.getPrice(),
                request.getRating(),
                request.getImageUrl(),
                request.getTimezone()
        );
    }

    public Unit mapForUpdateFrom(Unit unit, Map<String, String> updatedFields) {
        String region = (updatedFields.containsKey(REGION)) ? updatedFields.get(REGION) : unit.getRegion();
        String description = (updatedFields.containsKey(DESCRIPTION)) ? updatedFields.get(DESCRIPTION) : unit.getDescription();
        String cancellationPolicy = (updatedFields.containsKey(CANCELLATION_POLICY)) ? updatedFields.get(CANCELLATION_POLICY) : unit.getCancellationPolicy();
        int price = (updatedFields.containsKey(PRICE)) ? Integer.parseInt(updatedFields.get(PRICE)) : unit.getPrice();
        int rating = (updatedFields.containsKey(RATING)) ? Integer.parseInt(updatedFields.get(RATING)) : unit.getRating();
        String imageUrl = (updatedFields.containsKey(IMAGE_URL)) ? updatedFields.get(IMAGE_URL) : unit.getImageUrl();

        return new Unit(
                unit.getId(),
                unit.getTitle(),
                region,
                description,
                cancellationPolicy,
                price,
                rating,
                imageUrl,
                unit.getTimezone()
        );
    }
}
