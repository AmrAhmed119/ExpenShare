package com.expenshare.service.Settlement;

import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.model.enums.Status;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class SettlementSpecifications {
    public static PredicateSpecification<SettlementEntity> hasExpenseId(Long expenseId) {
        return (root, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("expense").get("id"), expenseId);
    }

    public static PredicateSpecification<SettlementEntity> hasStatus(Status status) {
        return (root, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static PredicateSpecification<SettlementEntity> hasFromUserId(Long fromUserId) {
        return (root, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("fromUser").get("id"), fromUserId);
    }

    public static PredicateSpecification<SettlementEntity> hasToUserId(Long toUserId) {
        return (root, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("toUser").get("id"), toUserId);
    }
}
