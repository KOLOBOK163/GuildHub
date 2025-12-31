package com.guildhub.Controller;

import com.guildhub.Dto.Response.JwtResponse;
import com.guildhub.Dto.Request.LoginRequest;
import com.guildhub.Dto.Request.RegisterRequest;
import com.guildhub.Entity.User;
import com.guildhub.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> signUp(@Valid @RequestBody RegisterRequest registerRequest)
    {
        return ResponseEntity.ok(authService.signUp(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody LoginRequest loginRequest)
    {
        return ResponseEntity.ok(authService.signIn(loginRequest));
    }


}
