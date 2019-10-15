package com.pefthymiou.mars.booking.api;

import com.pefthymiou.mars.booking.domain.CreateBookingRequestDto;
import com.pefthymiou.mars.booking.domain.actions.create.BookingService;
import com.pefthymiou.mars.unit.api.UriBuilder;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.Resource;
import com.pefthymiou.mars.unit.domain.UnitNotFoundException;
import com.pefthymiou.mars.unit.domain.UnprocessableRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("${api.basepath}/booking")
public class BookingController {

    private static final String ID_PATH = "/{id}";

    private BookingService<Resource> bookingService;
    private UriBuilder uriBuilder;

    @Autowired
    public BookingController(BookingService<Resource> bookingService, UriBuilder uriBuilder) {
        this.bookingService = bookingService;
        this.uriBuilder = uriBuilder;
    }

    @PostMapping(
            consumes = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity create(@RequestBody CreateBookingRequestDto request) {
        try {
            long id = bookingService.createFrom(request);
            return createdResponse(id);
        } catch (UnitNotFoundException e) {
            return errorResponseWith(e.getMessage(), NOT_FOUND);
        } catch (InvalidRequestException e) {
            return errorResponseWith(e.getMessage(), BAD_REQUEST);
        } catch (UnprocessableRequestException e) {
            return errorResponseWith(e.getMessage(), UNPROCESSABLE_ENTITY);
        }
    }

    private ResponseEntity createdResponse(long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.buildWith(ID_PATH, id));

        return new ResponseEntity<>(
                null,
                headers,
                CREATED
        );
    }

    private ResponseEntity errorResponseWith(String msg, HttpStatus statusCode) {
        return new ResponseEntity<>(
                msg,
                null,
                statusCode
        );
    }
}
