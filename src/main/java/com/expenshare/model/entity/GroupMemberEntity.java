package com.expenshare.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "group_members",
    uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "user_id"})
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GroupMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_group"))
    private GroupEntity group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user"))
    private UserEntity user;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;
}
