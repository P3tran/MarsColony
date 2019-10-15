package com.pefthymiou.mars.unit.domain.actions.crud;

import com.pefthymiou.mars.unit.domain.RequestDto;
import com.pefthymiou.mars.unit.domain.Resource;

import java.util.Map;

public interface CrudService<T extends Resource> {

    T retrieve(long id);

    long createFrom(RequestDto request);

    void deleteById(long id);

    void update(long id, Map<String, String> updatedFields);
}
