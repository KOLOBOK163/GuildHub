package com.guildhub.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchResultDto {
    private Long id;
    
    @NotNull(message = "Team ID is required")
    private Long teamId;
    
    private Long opponentTeamId;
    private String opponentName;
    
    @NotNull(message = "Match date is required")
    private LocalDateTime matchDate;
    
    private String map;
    private Integer teamScore;
    private Integer opponentScore;
    private String tournament;
    private String matchType;
    private String result;
    private String description;
}


