package com.guildhub.Service;

import com.guildhub.Dto.AdminCreateUserDto;
import com.guildhub.Dto.UserDto;
import com.guildhub.Entity.Role.UserRole;
import com.guildhub.Entity.User;
import com.guildhub.Mapper.UserMapper;
import com.guildhub.Reposirtory.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createByAdmin(AdminCreateUserDto adminCreateUserDto) {
        if(userRepository.existsByUsername(adminCreateUserDto.getUsername()))
        {
            throw new EntityExistsException("Пользователь с username" + adminCreateUserDto.getUsername() +  " уже существует");
        }

        if(userRepository.existsByEmail(adminCreateUserDto.getEmail()))
        {
            throw new EntityExistsException("Пользователь с email" + adminCreateUserDto.getEmail() +  " уже существует");
        }

        User user = new User();
        user.setUsername(adminCreateUserDto.getUsername());
        user.setEmail(adminCreateUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(adminCreateUserDto.getPassword()));

        if (adminCreateUserDto.getRoles() != null && !adminCreateUserDto.getRoles().isEmpty()) {
            user.setRoles(adminCreateUserDto.getRoles());
        } else {
            user.setRoles(List.of(UserRole.FAN));
        }

        return userMapper.UserPojoToUserDto(userRepository.save(user));
    }

    @Override
    public void delete(Long userId) {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        userRepository.deleteById(userId);
    }

    @Override
    public UserDto update(Long userId, UserDto userUpdateDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.equals(UserRole.ADMIN));
        if (!isAdmin && !Objects.equals(currentUser.getId(), userId)) {
            throw new AccessDeniedException("Cannot update another user's profile");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!user.getUsername().equals(userUpdateDto.getUsername()) &&
                userRepository.existsByUsername(userUpdateDto.getUsername())) {
            throw new EntityExistsException("Username already taken");
        }

        if (!user.getEmail().equals(userUpdateDto.getEmail()) &&
                userRepository.existsByEmail(userUpdateDto.getEmail())) {
            throw new EntityExistsException("Email already taken");
        }

        user.setUsername(userUpdateDto.getUsername());

        user.setEmail(userUpdateDto.getEmail());

        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }
        user = userRepository.save(user);
        return userMapper.UserPojoToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::UserPojoToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long userId)
    {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + userId));
    }

}
