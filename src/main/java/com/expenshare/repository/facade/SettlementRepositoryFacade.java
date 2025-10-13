package com.expenshare.repository.facade;

import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.repository.SettlementRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

@Transactional
@Singleton
public class SettlementRepositoryFacade {
    private final SettlementRepository settlementRepository;

    public SettlementRepositoryFacade(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    public SettlementEntity save(SettlementEntity settlement) {
        return settlementRepository.save(settlement);
    }
}
