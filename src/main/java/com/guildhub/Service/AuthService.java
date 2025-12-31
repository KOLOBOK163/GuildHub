package com.guildhub.Service;

import com.guildhub.Dto.Response.JwtResponse;
import com.guildhub.Dto.Request.LoginRequest;
import com.guildhub.Dto.Request.RegisterRequest;
import com.guildhub.Entity.User;

public interface AuthService {
    User signUp(RegisterRequest registerRequest);
    JwtResponse signIn(LoginRequest loginRequest);
}
