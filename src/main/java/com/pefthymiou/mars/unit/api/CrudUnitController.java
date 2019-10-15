package com.pefthymiou.mars.unit.api;

import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.Resource;
import com.pefthymiou.mars.unit.domain.UnitNotFoundException;
import com.pefthymiou.mars.unit.domain.UnprocessableRequestException;
import com.pefthymiou.mars.unit.domain.actions.crud.CreateUnitRequestDto;
import com.pefthymiou.mars.unit.domain.actions.crud.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("${api.basepath}/unit")
public class CrudUnitController {

    private static final String ID_PATH = "/{id}";

    private CrudService<Resource> service;
    private UriBuilder uriBuilder;

    @Autowired
    public CrudUnitController(CrudService<Resource> service, UriBuilder uriBuilder) {
        this.service = service;
        this.uriBuilder = uriBuilder;
    }

    @GetMapping("/{id}")
    public ResponseEntity retrieve(@PathVariable("id") long id) {
        try {
            Resource resource = service.retrieve(id);
            return okResponse(resource);
        } catch (UnitNotFoundException e) {
            return errorResponseWith(e.getMessage(), NOT_FOUND);
        }
    }

    @PostMapping(
            consumes = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity create(@RequestBody CreateUnitRequestDto request) {
        try {
            long id = service.createFrom(request);
            return createdResponse(id);
        } catch (InvalidRequestException e) {
            return errorResponseWith(e.getMessage(), BAD_REQUEST);
        } catch (UnprocessableRequestException e) {
            return errorResponseWith(e.getMessage(), UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") long id) {
        try {
            service.deleteById(id);
            return noContentResponse();
        } catch (UnitNotFoundException e) {
            return errorResponseWith(e.getMessage(), NOT_FOUND);
        } catch (UnprocessableRequestException e) {
            return errorResponseWith(e.getMessage(), UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") long id, @RequestBody Map<String, String> fields) {
        try {
            service.update(id, fields);
            return noContentWithLocationResponse();
        } catch (InvalidRequestException e) {
            return errorResponseWith(e.getMessage(), UNPROCESSABLE_ENTITY);
        } catch (UnitNotFoundException e) {
            return errorResponseWith(e.getMessage(), NOT_FOUND);
        }
    }

    private ResponseEntity okResponse(Resource resource) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);

        return new ResponseEntity<>(
                resource,
                headers,
                OK
        );
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

    private ResponseEntity noContentWithLocationResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.current());

        return new ResponseEntity<>(
                null,
                headers,
                NO_CONTENT
        );
    }

    private ResponseEntity noContentResponse() {
        return new ResponseEntity<>(
                null,
                null,
                NO_CONTENT
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
