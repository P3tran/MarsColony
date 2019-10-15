package com.pefthymiou.mars.unit;

import com.google.common.collect.ImmutableMap;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.RequestDto;
import com.pefthymiou.mars.unit.domain.UnitValidator;
import com.pefthymiou.mars.unit.domain.actions.crud.CreateUnitRequestDto;
import org.junit.Test;

import java.util.Map;

public class UnitValidatorShould {

    private static final Map<String, String> UPDATED_FIELDS_WITH_VALID_FIELDS = ImmutableMap.of("price", "40", "rating", "3");
    private static final Map<String, String> UPDATED_FIELDS_WITH_NON_POSITIVE_PRICE = ImmutableMap.of("price", "-1");
    private static final Map<String, String> UPDATED_FIELDS_WITH_NON_POSITIVE_RATING = ImmutableMap.of("rating", "-1");
    private static final Map<String, String> UPDATED_FIELDS_WITH_RATING_GREATER_THAN_FIVE = ImmutableMap.of("rating", "6");
    private static final Map<String, String> UPDATED_FIELDS_WITH_NON_NUMBER_PRICE = ImmutableMap.of("price", "invalid price");
    private static final Map<String, String> UPDATED_FIELDS_WITH_NON_NUMBER_RATING = ImmutableMap.of("rating", "invalid rating");

    private UnitValidator unitValidator = new UnitValidator();

    @Test
    public void notThrowInvalidRequestExceptionWhenRequestIsValid() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "title",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRequestIsNull() {
        unitValidator.validate(null);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRequestIsNotACreateUnitRequestDto() {
        unitValidator.validate(new UnexpectedRequest());
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenTitleIsNull() {
        unitValidator.validate(new CreateUnitRequestDto(
                        null,
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenTitleIsEmpty() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRegionIsNull() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        null,
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRegionIsEmpty() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "",
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenDescriptionIsNull() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        null,
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenDescriptionIsEmpty() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "",
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCancellationPolicyIsNull() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        null,
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenCancellationPolicyIsEmpty() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        "",
                        500,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenImageUrlIsNull() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        null,
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenImageUrlIsEmpty() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        "",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenPriceIsNonPositive() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        0,
                        4,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRatingIsNonPositive() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        0,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRatingIsGreaterThanFive() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        6,
                        "an image url",
                        "UTC"
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenTimezoneIsNull() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        null
                )
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenTimezoneIsInvalid() {
        unitValidator.validate(new CreateUnitRequestDto(
                        "a title",
                        "a region",
                        "a description",
                        "a cancellation policy",
                        500,
                        4,
                        "an image url",
                        "Invalid timezone"
                )
        );
    }

    @Test
    public void notThrowInvalidRequestExceptionWhenFieldsAreValidWhenValidatingForUpdate() {
        unitValidator.validateForUpdate(UPDATED_FIELDS_WITH_VALID_FIELDS);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenPriceIsNonPositiveWhenValidatingForUpdate() {
        unitValidator.validateForUpdate(UPDATED_FIELDS_WITH_NON_POSITIVE_PRICE);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRatingIsNonPositiveWhenValidatingForUpdate() {
        unitValidator.validateForUpdate(UPDATED_FIELDS_WITH_NON_POSITIVE_RATING);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRatingIsGreaterThanFiveWhenValidatingForUpdate() {
        unitValidator.validateForUpdate(UPDATED_FIELDS_WITH_RATING_GREATER_THAN_FIVE);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenPriceIsNotANumberWhenValidatingForUpdate() {
        unitValidator.validateForUpdate(UPDATED_FIELDS_WITH_NON_NUMBER_PRICE);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRatingIsNotANumberFiveWhenValidatingForUpdate() {
        unitValidator.validateForUpdate(UPDATED_FIELDS_WITH_NON_NUMBER_RATING);
    }

    private class UnexpectedRequest implements RequestDto {
    }
}