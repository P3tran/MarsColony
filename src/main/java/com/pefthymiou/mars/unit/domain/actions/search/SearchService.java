package com.pefthymiou.mars.unit.domain.actions.search;

import com.pefthymiou.mars.unit.domain.Resource;

import java.util.List;

public interface SearchService<T extends Resource> {

    List<T> search(String query);
}
