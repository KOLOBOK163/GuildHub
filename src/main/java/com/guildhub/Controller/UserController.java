package com.guildhub.Controller;

import com.guildhub.Dto.UserDto;
import com.guildhub.Entity.User;
import com.guildhub.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {
        return ResponseEntity.ok(userService.create(userDto));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto)
    {
        return ResponseEntity.ok(userService.update(userId, userDto));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId)
    {
        userService.delete(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId)
    {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
