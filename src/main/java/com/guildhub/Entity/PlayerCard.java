package com.guildhub.Entity;

import com.guildhub.Validation.FaceitUrl;
import com.guildhub.Validation.SteamId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "player_cards")
public class PlayerCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String fullName;

    @Column(name = "steam_id")
    @SteamId
    private String steamId;

    @Column(name = "faceit_url")
    @FaceitUrl
    private String faceitUrl;

    private int prizes;

    private int age;

    private String country;

    private Double rating;

    @Column(name = "kd_ratio")
    private Double kdRatio;

    @Column(name = "maps_played")
    private Integer mapsPlayed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "playerCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendance = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
