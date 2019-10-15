package com.pefthymiou.mars.unit.api;

import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.Resource;
import com.pefthymiou.mars.unit.domain.actions.browse.PagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping("${api.basepath}/units/browse")
public class BrowseUnitController {

    private PagingService<Resource> pagingService;

    @Autowired
    public BrowseUnitController(PagingService<Resource> pagingService) {
        this.pagingService = pagingService;
    }

    @GetMapping
    public ResponseEntity browse(@RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            Page<Resource> units = pagingService.browse(page, size);
            return okResponseWith(units);
        } catch (InvalidRequestException e) {
            return errorResponseWith(e.getMessage(), BAD_REQUEST);
        }
    }

    private ResponseEntity okResponseWith(Page<Resource> units) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);

        return new ResponseEntity<>(
                units,
                headers,
                OK
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
