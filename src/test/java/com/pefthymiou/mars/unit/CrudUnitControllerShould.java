package com.pefthymiou.mars.unit;

import com.pefthymiou.mars.unit.api.CrudUnitController;
import com.pefthymiou.mars.unit.api.UriBuilder;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.Resource;
import com.pefthymiou.mars.unit.domain.UnitNotFoundException;
import com.pefthymiou.mars.unit.domain.UnprocessableRequestException;
import com.pefthymiou.mars.unit.domain.actions.crud.CreateUnitRequestDto;
import com.pefthymiou.mars.unit.domain.actions.crud.CrudService;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.pefthymiou.mars.base.builders.UnitBuilder.aUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

public class CrudUnitControllerShould {

    private static final long ID = 1;
    private static final Unit unit = aUnit().build();
    private static final Map<String, String> UPDATED_FIELDS = new HashMap<>();
    private static final String UNIT_NOT_FOUND_MESSAGE = "Requested unit was not found";
    private static final String INVALID_REQUEST = "Invalid request";
    private static final String TITLE_EXISTS = "This title already exist";
    private static final String CANNOT_DELETE_UNIT_WITH_BOOKINGS = "The unit cannot be deleted, because there are bookings for it";

    private CrudUnitController crudUnitController;

    @Mock
    private CrudService<Resource> unitService;
    @Mock
    private CreateUnitRequestDto request;
    @Mock
    private UriBuilder uriBuilder;

    @Before
    public void setup() {
        initMocks(this);
        crudUnitController = new CrudUnitController(unitService, uriBuilder);
    }

    @Test
    public void returnErrorWhenUnitWasNotFoundWhenRetrievingAUnit() {
        when(unitService.retrieve(ID)).thenThrow(new UnitNotFoundException(UNIT_NOT_FOUND_MESSAGE));

        ResponseEntity response = crudUnitController.retrieve(ID);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(UNIT_NOT_FOUND_MESSAGE);
    }

    @Test
    public void returnAnExistingUnit() {
        when(unitService.retrieve(ID)).thenReturn(unit);

        ResponseEntity response = crudUnitController.retrieve(ID);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON_UTF8);
        assertThat(response.getBody()).isEqualToComparingFieldByField(unit);
    }

    @Test
    public void returnErrorWhenRequestIsInvalidWhenCreatingAUnit() {
        when(unitService.createFrom(request)).thenThrow(new InvalidRequestException(INVALID_REQUEST));

        ResponseEntity response = crudUnitController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    public void returnErrorWhenRequestIsUnprocessableWhenCreatingAUnit() {
        when(unitService.createFrom(request)).thenThrow(new UnprocessableRequestException(TITLE_EXISTS));

        ResponseEntity response = crudUnitController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo(TITLE_EXISTS);
    }

    @Test
    public void createANewUnit() throws URISyntaxException {
        URI uri = new URI("http://localhost:8080/api/v1/unit/1");
        when(unitService.createFrom(request)).thenReturn(ID);
        when(uriBuilder.buildWith("/{id}", ID)).thenReturn(uri);

        ResponseEntity response = crudUnitController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(uri);
    }

    @Test
    public void returnErrorWhenUnitWasNotFoundWhenDeletingAUnit() {
        doThrow(new UnitNotFoundException(UNIT_NOT_FOUND_MESSAGE)).when(unitService).deleteById(ID);

        ResponseEntity response = crudUnitController.delete(ID);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(UNIT_NOT_FOUND_MESSAGE);
    }

    @Test
    public void returnErrorWhenUnitHasBookingsWhenDeletingAUnit() {
        doThrow(new UnprocessableRequestException(CANNOT_DELETE_UNIT_WITH_BOOKINGS)).when(unitService).deleteById(ID);

        ResponseEntity response = crudUnitController.delete(ID);

        assertThat(response.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo(CANNOT_DELETE_UNIT_WITH_BOOKINGS);
    }

    @Test
    public void deleteAnExistingUnit() {
        ResponseEntity response = crudUnitController.delete(ID);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void returnErrorWhenRequestIsInvalidWhenUpdatingAUnit() {
        doThrow(new InvalidRequestException(INVALID_REQUEST)).when(unitService).update(ID, UPDATED_FIELDS);

        ResponseEntity response = crudUnitController.update(ID, UPDATED_FIELDS);

        assertThat(response.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo(INVALID_REQUEST);
    }
    
    @Test
    public void returnErrorWhenUnitWasNotFoundWhenUpdatingAUnit() {
        doThrow(new UnitNotFoundException(UNIT_NOT_FOUND_MESSAGE)).when(unitService).update(ID, UPDATED_FIELDS);

        ResponseEntity response = crudUnitController.update(ID, UPDATED_FIELDS);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(UNIT_NOT_FOUND_MESSAGE);
    }

    @Test
    public void updateAnExistingUnit() throws URISyntaxException {
        URI uri = new URI("http://localhost:8080/api/v1/unit/1");
        when(uriBuilder.current()).thenReturn(uri);

        ResponseEntity response = crudUnitController.update(ID, UPDATED_FIELDS);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(response.getHeaders().getLocation()).isEqualTo(uri);
    }
}
