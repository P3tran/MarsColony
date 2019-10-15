package com.pefthymiou.mars.unit;

import com.pefthymiou.mars.unit.api.SearchUnitController;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.actions.search.SearchService;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.pefthymiou.mars.base.builders.UnitBuilder.aUnit;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.*;

public class SearchUnitControllerShould {

    private static final String INVALID_QUERY = "a";
    private static final String NO_RESULTS_QUERY = "this query matches no results";
    private static final String QUERY = "athens";
    private static final String INVALID_REQUEST_MESSAGE = "The size of the query should be at least 2";
    private static final List<Unit> RESULTS = asList(aUnit().build(), aUnit().build());

    private SearchUnitController searchUnitController;

    @Mock
    private SearchService searchService;

    @Before
    public void setup() {
        initMocks(this);
        searchUnitController = new SearchUnitController(searchService);
    }

    @Test
    public void returnErrorWhenRequestIsInvalid() {
        when(searchService.search(INVALID_QUERY)).thenThrow(new InvalidRequestException(INVALID_REQUEST_MESSAGE));

        ResponseEntity response = searchUnitController.search(INVALID_QUERY);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(INVALID_REQUEST_MESSAGE);
    }

    @Test
    public void returnEmptyListWhenThereAreNoMatchingResults() {
        when(searchService.search(NO_RESULTS_QUERY)).thenReturn(emptyList());

        ResponseEntity response = searchUnitController.search(NO_RESULTS_QUERY);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void returnSearchResults() {
        when(searchService.search(QUERY)).thenReturn(RESULTS);

        ResponseEntity response = searchUnitController.search(QUERY);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(((List<Unit>)response.getBody()).size()).isEqualTo(2);
        assertThat(((List<Unit>)response.getBody()).get(0)).isEqualTo((RESULTS.get(0)));
        assertThat(((List<Unit>)response.getBody()).get(1)).isEqualTo((RESULTS.get(1)));
    }
}
