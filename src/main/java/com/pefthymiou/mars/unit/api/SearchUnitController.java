package com.pefthymiou.mars.unit.api;

import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.Resource;
import com.pefthymiou.mars.unit.domain.actions.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.basepath}/units")
public class SearchUnitController {

    private SearchService<Resource> searchService;

    @Autowired
    public SearchUnitController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity search(@RequestParam("q") String query) {
        try {
            List<Resource> results = searchService.search(query);
            if (results.isEmpty())
                return responseWith(results, NO_CONTENT);
            else
                return responseWith(results, OK);
        } catch (InvalidRequestException e) {
            return errorResponseWith(e.getMessage(), BAD_REQUEST);
        }
    }

    private ResponseEntity responseWith(List<Resource> results, HttpStatus statusCode) {
        return new ResponseEntity<>(
                results,
                null,
                statusCode
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
