package com.pefthymiou.mars.unit.infrastructure.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {

    List<Unit> findByTitleIgnoreCaseContainingOrRegionIgnoreCaseContaining(String title, String region);

    boolean existsByTitle(String title);
}
