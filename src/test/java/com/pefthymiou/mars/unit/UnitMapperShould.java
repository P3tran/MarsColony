package com.pefthymiou.mars.unit;

import com.google.common.collect.ImmutableMap;
import com.pefthymiou.mars.unit.domain.UnitMapper;
import com.pefthymiou.mars.unit.domain.actions.crud.CreateUnitRequestDto;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import org.junit.Test;

import java.util.Map;

import static com.pefthymiou.mars.base.builders.UnitBuilder.aUnit;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class UnitMapperShould {

    private static final Map<String, String> updatedFields = ImmutableMap.of(
            "description", "an updated description",
            "cancellationPolicy", "an updated cancellation policy",
            "rating", "4"
    );

    private UnitMapper unitMapper = new UnitMapper();

    private CreateUnitRequestDto request = new CreateUnitRequestDto(
            "a title",
            "a region",
            "a description",
            "a cancellation policy",
            500,
            4,
            "an image url",
            "UTC"
    );
    private Unit expectedUnit = aUnit()
            .withTitle("a title")
            .withRegion("a region")
            .withDescription("a description")
            .withCancellationPolicy("a cancellation policy")
            .withPrice(500)
            .withRating(4)
            .withImageUrl("an image url")
            .withTimezone("UTC")
            .build();
    private Unit unit = aUnit()
            .withTitle("a title")
            .withRegion("a region")
            .withDescription("a description")
            .withCancellationPolicy("a cancellation policy")
            .withPrice(500)
            .withRating(3)
            .withImageUrl("an image url")
            .withTimezone("UTC")
            .build();
    private Unit expectedUnitWithUpdatedFields = aUnit()
            .withTitle("a title")
            .withRegion("a region")
            .withDescription("an updated description")
            .withCancellationPolicy("an updated cancellation policy")
            .withPrice(500)
            .withRating(4)
            .withImageUrl("an image url")
            .withTimezone("UTC")
            .build();

    @Test
    public void mapACreateUnitRequestDtoToUnit() {
        Unit actualUnit = unitMapper.mapFrom(request);

        assertThat(actualUnit).isEqualToIgnoringGivenFields(expectedUnit, "id");
    }

    @Test
    public void mapUpdatedFieldsToExistingUnit() {
        Unit actualUnitWithUpdatedFields = unitMapper.mapForUpdateFrom(unit, updatedFields);

        assertThat(actualUnitWithUpdatedFields).isEqualToIgnoringGivenFields(expectedUnitWithUpdatedFields, "id");
    }
}
