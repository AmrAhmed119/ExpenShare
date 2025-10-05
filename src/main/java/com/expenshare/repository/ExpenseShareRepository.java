package com.expenshare.repository;

import com.expenshare.model.entity.ExpenseShareEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ExpenseShareRepository extends CrudRepository<ExpenseShareEntity, Long> {
}
