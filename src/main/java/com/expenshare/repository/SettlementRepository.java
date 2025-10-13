package com.expenshare.repository;

import com.expenshare.model.entity.SettlementEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface SettlementRepository extends CrudRepository<SettlementEntity, Long> {
}
