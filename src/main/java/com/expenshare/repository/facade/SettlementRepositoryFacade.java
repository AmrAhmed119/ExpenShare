package com.expenshare.repository.facade;

import com.expenshare.exception.NotFoundException;
import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.repository.SettlementRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

@Transactional
@Singleton
public class SettlementRepositoryFacade {
    private final SettlementRepository settlementRepository;

    public SettlementRepositoryFacade(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    public SettlementEntity getOrThrow(Long id) {
        return settlementRepository.findById(id).orElseThrow(() -> new NotFoundException("Settlement not found"));
    }

    public SettlementEntity save(SettlementEntity settlement) {
        return settlementRepository.save(settlement);
    }

    public SettlementEntity update(SettlementEntity settlement) {
        return settlementRepository.update(settlement);
    }

    public Page<SettlementEntity> filterExpenseSettlements(
        PredicateSpecification<SettlementEntity> specs,
        Pageable pageable
    ) {
        return settlementRepository.findAll(specs, pageable);
    }
}
