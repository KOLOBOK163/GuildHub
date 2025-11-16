package com.guildhub.Service;

import com.guildhub.Dto.JwtResponse;
import com.guildhub.Dto.UserDto;
import com.guildhub.Entity.Role.UserRole;
import com.guildhub.Entity.User;
import com.guildhub.Mapper.UserMapper;
import com.guildhub.Reposirtory.UserRepository;
import com.guildhub.Security.JwtUtils;
import com.guildhub.Security.UserDetailsImpl;
import jakarta.persistence.EntityExistsException;
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
    public User signUp(UserDto userDto) {

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new EntityExistsException("User with this name already exists");
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EntityExistsException("User with this email already exists");
        }

        User user = userMapper.UserDtoToUserPojo(userDto);
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singletonList(UserRole.FAN));

        return userRepository.save(user);
    }

    @Override
    public JwtResponse signIn(UserDto userDto) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getUsername(),
                            userDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();

            userRepository.save(user);

            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setAccessToken(accessToken);
            jwtResponse.setUsername(user.getUsername());
            jwtResponse.setEmail(user.getEmail());
            jwtResponse.setRoles(user.getRoles());

            return jwtResponse;
    }
}
