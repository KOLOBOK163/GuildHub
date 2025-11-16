package com.guildhub.Service;

import com.guildhub.Dto.JwtResponse;
import com.guildhub.Dto.UserDto;
import com.guildhub.Entity.User;

public interface AuthService {
    User signUp(UserDto userDto);
    JwtResponse signIn(UserDto userDto);
}
