package com.guildhub.Entity;

import com.guildhub.Entity.Role.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.annotation.ElementType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, unique = true, nullable = false)
    private String username;

    @Column(length = 30, unique = true, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_card_data_id")
    private PlayerCard playerCardData;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private List<UserRole> roles;
}
