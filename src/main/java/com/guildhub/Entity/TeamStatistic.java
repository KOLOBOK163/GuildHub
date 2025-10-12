package com.guildhub.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "team_statistics")
public class TeamStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private LocalDate periodStart;

    private LocalDate periodEnd;

    private Integer matchesPlayed;

    private Integer matchesWon;

    private Integer matchesLost;

    private Double winRate;

    private Double averageRating;

    private Integer totalRounds;

    private Integer roundsWon;

    private Integer roundsLost;

    private Double roundWinRate;

    private String mostPlayedMap;

    private String bestMap;

    private String worstMap;

    private Double averageKD;

    private Integer totalKills;

    private Integer totalDeaths;

    private Integer totalAssists;
}
