package com.pefthymiou.mars.unit;

import com.pefthymiou.mars.booking.infrastructure.db.BookingRepository;
import com.pefthymiou.mars.unit.domain.*;
import com.pefthymiou.mars.unit.domain.actions.crud.CreateUnitRequestDto;
import com.pefthymiou.mars.unit.domain.actions.crud.CrudUnitService;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import com.pefthymiou.mars.unit.infrastructure.db.UnitRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.pefthymiou.mars.base.builders.UnitBuilder.aUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CrudUnitServiceShould {

    private static final long ID = 1;
    private static final Unit UNIT = aUnit().build();
    private static final Unit UPDATED_UNIT = aUnit().build();
    private static final Map<String, String> UPDATED_FIELDS = new HashMap<>();
    private static final String INVALID_REQUEST = "Invalid request";

    private CrudUnitService crudUnitService;

    @Mock
    private UnitRepository unitRepository;
    @Mock
    private CreateUnitRequestDto request;
    @Mock
    private UnitValidator unitValidator;
    @Mock
    private UnitMapper unitMapper;
    @Mock
    private BookingRepository bookingRepository;

    @Before
    public void setup() {
        initMocksBehavior();
        crudUnitService = new CrudUnitService(unitRepository, unitValidator, unitMapper, bookingRepository);
    }

    @Test(expected = UnitNotFoundException.class)
    public void throwUnitNotFoundExceptionWhenUnitDoesNotExistWhenRetrievingAUnit() {
        when(unitRepository.findById(ID)).thenReturn(Optional.empty());

        crudUnitService.retrieve(ID);
    }

    @Test
    public void retrieveAUnit() {
        Resource retrievedUnit = crudUnitService.retrieve(ID);

        assertThat(retrievedUnit).isEqualTo(UNIT);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRequestIsInvalidWhenCreatingAUnit() {
        doThrow(new InvalidRequestException(INVALID_REQUEST)).when(unitValidator).validate(request);

        crudUnitService.createFrom(request);
    }

    @Test(expected = UnprocessableRequestException.class)
    public void throwUnprocessableRequestExceptionWhenTitleExistsWhenCreatingAUnit() {
        when(unitRepository.existsByTitle(request.getTitle())).thenReturn(true);

        crudUnitService.createFrom(request);
    }

    @Test
    public void createANewUnit() {
        crudUnitService.createFrom(request);

        verify(unitRepository).save(UNIT);
    }

    @Test
    public void returnTheIdOfTheNewlyCreatedUnit() {
        long actualId = crudUnitService.createFrom(request);

        assertThat(actualId).isEqualTo(UNIT.getId());
    }

    @Test(expected = UnitNotFoundException.class)
    public void throwUnitNotFoundExceptionWhenUnitDoesNotExistWhenDeletingAUnit() {
        when(unitRepository.existsById(ID)).thenReturn(false);

        crudUnitService.deleteById(ID);
    }

    @Test(expected = UnprocessableRequestException.class)
    public void throwUnprocessableRequestExceptionWhenAttemptingToDeleteAUnitWithBookings() {
        when(bookingRepository.existsByUnitId(ID)).thenReturn(true);

        crudUnitService.deleteById(ID);
    }

    @Test
    public void deleteAnExistingUnit() {
        crudUnitService.deleteById(ID);

        verify(unitRepository).deleteById(ID);
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRequestIsInvalidWhenUpdatingAUnit() {
        doThrow(new InvalidRequestException(INVALID_REQUEST)).when(unitValidator).validateForUpdate(UPDATED_FIELDS);

        crudUnitService.update(ID, UPDATED_FIELDS);
    }

    @Test(expected = UnitNotFoundException.class)
    public void throwUnitNotFoundExceptionWhenUnitDoesNotExistWhenUpdatingAUnit() {
        when(unitRepository.findById(ID)).thenReturn(Optional.empty());

        crudUnitService.update(ID, UPDATED_FIELDS);
    }

    @Test
    public void updateAnExistingUnit() {
        when(unitMapper.mapForUpdateFrom(UNIT, UPDATED_FIELDS)).thenReturn(UPDATED_UNIT);

        crudUnitService.update(ID, UPDATED_FIELDS);

        verify(unitRepository).save(UPDATED_UNIT);
    }

    private void initMocksBehavior() {
        initMocks(this);
        when(unitMapper.mapFrom(request)).thenReturn(UNIT);
        when(unitRepository.save(UNIT)).thenReturn(UNIT);
        when(unitRepository.findById(ID)).thenReturn(Optional.of(UNIT));
        when(unitRepository.existsById(ID)).thenReturn(true);
        when(bookingRepository.existsByUnitId(ID)).thenReturn(false);
    }
}
