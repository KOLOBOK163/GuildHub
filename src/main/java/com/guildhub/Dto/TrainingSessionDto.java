package com.guildhub.Dto;

import com.guildhub.Entity.SessionType.SessionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TrainingSessionDto {
    private Long id;
    
    @NotNull(message = "Date and time is required")
    private LocalDateTime dateTime;
    
    private String description;
    
    @NotNull(message = "Session type is required")
    private SessionType type;
    
    @NotNull(message = "Team ID is required")
    private Long teamId;
    
    private String opponent;
    private String serverInfo;
}


