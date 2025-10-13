package com.expenshare.repository;

import com.expenshare.model.entity.SettlementEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

@Repository
public interface SettlementRepository extends PageableRepository<SettlementEntity, Long> {
    Page<SettlementEntity> findAll(PredicateSpecification<SettlementEntity> spec, Pageable pageable);
}
