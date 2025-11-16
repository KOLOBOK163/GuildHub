package com.guildhub.Controller;

import com.guildhub.Dto.JwtResponse;
import com.guildhub.Dto.UserDto;
import com.guildhub.Entity.User;
import com.guildhub.Security.JwtUtils;
import com.guildhub.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    public ResponseEntity<User> signUp(@RequestBody UserDto userDto)
    {
        return ResponseEntity.ok(authService.signUp(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> signIn(@RequestBody UserDto userDto)
    {
        return ResponseEntity.ok(authService.signIn(userDto));
    }

}
