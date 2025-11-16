package com.guildhub.Dto;

import com.guildhub.Entity.Role.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private List<UserRole> roles;
}
