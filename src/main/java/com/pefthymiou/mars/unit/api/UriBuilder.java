package com.pefthymiou.mars.unit.api;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class UriBuilder {

    public URI current() {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .buildAndExpand()
                .toUri();
    }

    public URI buildWith(String path, long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(path)
                .buildAndExpand(id)
                .toUri();
    }
}
