package com.pefthymiou.mars.unit.domain.actions.browse;

import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.infrastructure.db.UnitPagingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PagingUnitService implements PagingService {

    private UnitPagingRepository unitPagingRepository;

    @Autowired
    public PagingUnitService(UnitPagingRepository unitPagingRepository) {
        this.unitPagingRepository = unitPagingRepository;
    }

    @Override
    public Page browse(int page, int size) {
        validateParams(page, size);
        return unitPagingRepository.findAll(PageRequest.of(page, size));
    }

    private void validateParams(int page, int size) {
        if (page < 0)
            throw new InvalidRequestException("Page must be a non negative integer");
        if (size < 1)
            throw new InvalidRequestException("Size must be a positive integer");
    }
}
