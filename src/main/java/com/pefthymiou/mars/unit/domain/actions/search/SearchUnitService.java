package com.pefthymiou.mars.unit.domain.actions.search;

import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import com.pefthymiou.mars.unit.infrastructure.db.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUnitService implements SearchService {

    private static final String INVALID_QUERY_LENGTH = "Query length must be at least 2";

    private UnitRepository unitRepository;

    @Autowired
    public SearchUnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public List<Unit> search(String query) {
        validate(query);

        return unitRepository.findByTitleIgnoreCaseContainingOrRegionIgnoreCaseContaining(query, query);
    }

    private void validate(String query) {
        if (query.length() < 2)
            throw new InvalidRequestException(INVALID_QUERY_LENGTH);
    }
}
