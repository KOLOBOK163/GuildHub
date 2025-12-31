package com.guildhub.Service;

import com.guildhub.Dto.Response.JwtResponse;
import com.guildhub.Dto.Request.LoginRequest;
import com.guildhub.Dto.Request.RegisterRequest;
import com.guildhub.Entity.Role.UserRole;
import com.guildhub.Entity.User;
import com.guildhub.Mapper.UserMapper;
import com.guildhub.Reposirtory.UserRepository;
import com.guildhub.Security.JwtUtils;
import com.guildhub.Security.UserDetailsImpl;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User signUp(RegisterRequest registerRequest) {
        try {
            if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
                throw new EntityExistsException("User with this name already exists");
            }
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                throw new EntityExistsException("User with this email already exists");
            }

            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRoles(Collections.singletonList(UserRole.FAN));
            user.setAvatarUrl("https://example.com/default-avatar.png");
            return userRepository.save(user);
        }catch (EntityExistsException e)
        {
            throw new EntityExistsException(e.getMessage());
        }
    }

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();

            userRepository.save(user);

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .roles(user.getRoles())
                    .build();
    }
}
