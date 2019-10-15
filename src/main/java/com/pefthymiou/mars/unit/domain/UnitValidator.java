package com.pefthymiou.mars.unit.domain;

import com.pefthymiou.mars.unit.domain.actions.crud.CreateUnitRequestDto;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Map;

@Component
public class UnitValidator {

    private static final String INVALID_REQUEST = "Invalid request";
    private static final String INVALID_PRICE = "Price must be a positive integer";
    private static final String INVALID_RATING = "Rating must be between 1 and 5 (inclusive)";
    private static final String FIELD_MAY_NOT_BE_NULL = " may not be null";
    private static final String INVALID_TIMEZONE = "Timezone is invalid";

    public void validate(RequestDto request) {
        if (request == null)
            throw new InvalidRequestException(INVALID_REQUEST);

        CreateUnitRequestDto createUnitRequestDto = from(request);
        
        validateNotNullOrEmpty("title", createUnitRequestDto.getTitle());
        validateNotNullOrEmpty("region", createUnitRequestDto.getRegion());
        validateNotNullOrEmpty("description", createUnitRequestDto.getDescription());
        validateNotNullOrEmpty("cancellation policy", createUnitRequestDto.getCancellationPolicy());
        validateNotNullOrEmpty("image URL", createUnitRequestDto.getImageUrl());
        validateNotNullOrEmpty("timezone", createUnitRequestDto.getTimezone());

        validatePriceAndRating(createUnitRequestDto.getPrice(), createUnitRequestDto.getRating());
        validateTimezone(createUnitRequestDto.getTimezone());
    }

    public void validateForUpdate(Map<String, String> fields) {
        int price = 1;
        int rating = 1;

        try {
            if (fields.containsKey("price")) {
                price = Integer.parseInt(fields.get("price"));
            }
        } catch (NumberFormatException e) {
            throw new InvalidRequestException(INVALID_PRICE);
        }

        try {
            if (fields.containsKey("rating")) {
                rating = Integer.parseInt(fields.get("rating"));
            }
        } catch (NumberFormatException e) {
            throw new InvalidRequestException(INVALID_RATING);
        }

        validatePriceAndRating(price, rating);
    }

    private CreateUnitRequestDto from(RequestDto request) {
        try {
            return (CreateUnitRequestDto) request;
        } catch (ClassCastException e) {
            throw new InvalidRequestException(INVALID_REQUEST);
        }
    }

    private void validateNotNullOrEmpty(String fieldName, String... fields) {
        for (String field : fields)
            if (isNullOrEmpty(field))
                throw new InvalidRequestException(fieldName + FIELD_MAY_NOT_BE_NULL);
    }

    private boolean isNullOrEmpty(String field) {
        return field == null || field.isEmpty();
    }

    private void validatePriceAndRating(int price, int rating) {
        if (price < 1)
            throw new InvalidRequestException(INVALID_PRICE);

        if (rating < 1 || rating > 5)
            throw new InvalidRequestException(INVALID_RATING);
    }

    private void validateTimezone(String timezone) {
        try {
            ZoneId.of(timezone);
        } catch (DateTimeException e) {
            throw new InvalidRequestException(INVALID_TIMEZONE);
        }
    }
}
