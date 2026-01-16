package com.guildhub.Dto;

import com.guildhub.Entity.Role.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private String username;

    private String email;

    private String password;

    private String avatarUrl;

    private List<UserRole> roles;

}
