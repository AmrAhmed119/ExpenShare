package com.expenshare.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "expenses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_group"))
    private GroupEntity group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paid_by", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_user"))
    private UserEntity paidBy;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "split_type", nullable = false, length = 10)
    private SplitType splitType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<ExpenseShareEntity> shares = new HashSet<>();
}
