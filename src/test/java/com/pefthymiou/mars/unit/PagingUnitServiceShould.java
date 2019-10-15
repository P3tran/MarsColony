package com.pefthymiou.mars.unit;

import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.actions.browse.PagingUnitService;
import com.pefthymiou.mars.unit.infrastructure.db.UnitPagingRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PagingUnitServiceShould {

    private static final int PAGE = 1;
    private static final int SIZE = 1;
    private static final int INVALID_PAGE = -1;
    private static final int INVALID_SIZE = -1;

    private PagingUnitService pagingUnitService;

    @Mock
    private UnitPagingRepository unitPagingRepository;
    @Captor
    private ArgumentCaptor<Pageable> captor;

    @Before
    public void setup() {
        initMocks(this);
        pagingUnitService = new PagingUnitService(unitPagingRepository);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenPageIsInvalid() {
        pagingUnitService.browse(INVALID_PAGE, SIZE);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenSizeIsInvalid() {
        pagingUnitService.browse(PAGE, INVALID_SIZE);
    }

    @Test
    public void findAllUnits() {
        pagingUnitService.browse(PAGE, SIZE);

        verify(unitPagingRepository).findAll(captor.capture());
        assertThat(captor.getValue().getPageNumber()).isEqualTo(PAGE);
        assertThat(captor.getValue().getPageSize()).isEqualTo(SIZE);
    }
}
