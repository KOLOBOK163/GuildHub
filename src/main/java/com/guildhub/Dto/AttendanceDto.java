package com.guildhub.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceDto {
    private Long id;
    
    @NotNull(message = "Training session ID is required")
    private Long trainingSessionId;
    
    @NotNull(message = "Player card ID is required")
    private Long playerCardId;
    
    private boolean attended;
    private String reason;
    private Integer performance;
    private String notes;
}


