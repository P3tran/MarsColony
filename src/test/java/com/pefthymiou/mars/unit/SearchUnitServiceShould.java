package com.pefthymiou.mars.unit;

import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.actions.search.SearchUnitService;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import com.pefthymiou.mars.unit.infrastructure.db.UnitRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static com.pefthymiou.mars.base.builders.UnitBuilder.aUnit;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchUnitServiceShould {

    private static final String INVALID_QUERY = "a";
    private static final String QUERY = "athens";
    private static final Unit UNIT_1 = aUnit().build();
    private static final Unit UNIT_2 = aUnit().build();
    private static final List<Unit> RESULTS = asList(UNIT_1, UNIT_2);

    private SearchUnitService searchUnitService;

    @Mock
    private UnitRepository unitRepository;

    @Before
    public void setup() {
        initMocksBehavior();
        searchUnitService = new SearchUnitService(unitRepository);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenQuerySizeIsLessThanTwo() {
        searchUnitService.search(INVALID_QUERY);
    }

    @Test
    public void searchUnitWithTitleOrRegionMatchingTheSearchQuery() {
        searchUnitService.search(QUERY);

        verify(unitRepository).findByTitleIgnoreCaseContainingOrRegionIgnoreCaseContaining(QUERY, QUERY);
    }

    @Test
    public void returnAllUnitsMatchingEitherByTitleOrRegion() {
        List<Unit> actualUnits = searchUnitService.search(QUERY);

        assertThat(actualUnits).containsExactly(UNIT_1, UNIT_2);
    }

    private void initMocksBehavior() {
        initMocks(this);
        when(unitRepository.findByTitleIgnoreCaseContainingOrRegionIgnoreCaseContaining(QUERY, QUERY)).thenReturn(RESULTS);
    }
}
