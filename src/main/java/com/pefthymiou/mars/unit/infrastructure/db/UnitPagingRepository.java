package com.pefthymiou.mars.unit.infrastructure.db;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitPagingRepository extends PagingAndSortingRepository<Unit, Long> {

}
