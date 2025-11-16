package com.guildhub.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TeamStatisticDto {
    private Long id;
    
    @NotNull(message = "Team ID is required")
    private Long teamId;
    
    @NotNull(message = "Period start is required")
    private LocalDate periodStart;
    
    @NotNull(message = "Period end is required")
    private LocalDate periodEnd;
    
    private Integer matchesPlayed = 0;
    private Integer matchesWon = 0;
    private Integer matchesLost = 0;
    private Double winRate;
    private Double averageRating;
    private Integer totalRounds = 0;
    private Integer roundsWon = 0;
    private Integer roundsLost = 0;
    private Double roundWinRate;
    private String mostPlayedMap;
    private String bestMap;
    private String worstMap;
    private Double averageKD;
    private Integer totalKills = 0;
    private Integer totalDeaths = 0;
    private Integer totalAssists = 0;
}


