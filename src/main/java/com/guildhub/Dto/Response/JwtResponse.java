package com.guildhub.Dto.Response;

import com.guildhub.Entity.Role.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String username;
    private String email;
    private List<UserRole> roles;
}
