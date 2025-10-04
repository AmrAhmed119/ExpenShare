package com.expenshare.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_shares")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseShareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expense_id", nullable = false, foreignKey = @ForeignKey(name = "fk_share_expense"))
    private ExpenseEntity expense;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_share_user"))
    private UserEntity user;

    @Column(name = "share_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal shareAmount;
}
