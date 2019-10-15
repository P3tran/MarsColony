package com.pefthymiou.mars.booking;

import com.pefthymiou.mars.booking.api.BookingController;
import com.pefthymiou.mars.booking.domain.CreateBookingRequestDto;
import com.pefthymiou.mars.booking.domain.actions.create.BookingService;
import com.pefthymiou.mars.unit.api.UriBuilder;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.UnitNotFoundException;
import com.pefthymiou.mars.unit.domain.UnprocessableRequestException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.*;

public class BookingControllerShould {

    private static final long ID = 1;
    private static final String UNIT_NOT_FOUND = "Requested unit could not be found";
    private static final String INVALID_REQUEST = "Invalid request";
    private static final String UNIT_UNAVAILABLE = "Requested unit is unavailable for the selected dates";

    private BookingController bookingController;

    @Mock
    private BookingService bookingService;
    @Mock
    private CreateBookingRequestDto request;
    @Mock
    private UriBuilder uriBuilder;

    @Before
    public void setup() {
        initMocks(this);
        bookingController = new BookingController(bookingService, uriBuilder);
    }

    @Test
    public void returnErrorWhenUnitWasNotFound() {
        when(bookingService.createFrom(request)).thenThrow(new UnitNotFoundException(UNIT_NOT_FOUND));

        ResponseEntity response = bookingController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(UNIT_NOT_FOUND);
    }

    @Test
    public void returnErrorWhenRequestIsInvalid() {
        when(bookingService.createFrom(request)).thenThrow(new InvalidRequestException(INVALID_REQUEST));

        ResponseEntity response = bookingController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    public void returnErrorWhenRequestCouldNotBeProcessed() {
        when(bookingService.createFrom(request)).thenThrow(new UnprocessableRequestException(UNIT_UNAVAILABLE));

        ResponseEntity response = bookingController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo(UNIT_UNAVAILABLE);
    }

    @Test
    public void createANewBooking() throws URISyntaxException {
        URI uri = new URI("http://localhost:8080/api/v1/booking/1");
        when(bookingService.createFrom(request)).thenReturn(ID);
        when(uriBuilder.buildWith("/{id}", ID)).thenReturn(uri);

        ResponseEntity response = bookingController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(uri);
    }
}
