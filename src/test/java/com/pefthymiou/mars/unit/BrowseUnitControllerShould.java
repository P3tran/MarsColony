package com.pefthymiou.mars.unit;

import com.pefthymiou.mars.unit.api.BrowseUnitController;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.actions.browse.PagingService;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.pefthymiou.mars.base.builders.UnitBuilder.aUnit;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class BrowseUnitControllerShould {

    private static final int PAGE = 1;
    private static final int SIZE = 1;
    private static final String INVALID_REQUEST = "Page and size should be both positive integers";
    private static final List<Unit> UNITS = asList(aUnit().build(), aUnit().build());
    private static final Page<Unit> RESULT_PAGE = new PageImpl(UNITS);

    private BrowseUnitController browseUnitController;

    @Mock
    private PagingService pagingService;

    @Before
    public void setup() {
        initMocks(this);
        browseUnitController = new BrowseUnitController(pagingService);
    }

    @Test
    public void returnErrorWhenRequestIsInvalid() {
        when(pagingService.browse(PAGE, SIZE)).thenThrow(new InvalidRequestException(INVALID_REQUEST));

        ResponseEntity response = browseUnitController.browse(PAGE, SIZE);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    public void returnAPageOfUnits() {
        when(pagingService.browse(PAGE, SIZE)).thenReturn(RESULT_PAGE);

        ResponseEntity response = browseUnitController.browse(PAGE, SIZE);

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
