package com.guildhub.Controller;

import com.guildhub.Dto.AdminCreateUserDto;
import com.guildhub.Dto.UserDto;
import com.guildhub.Entity.User;
import com.guildhub.Service.ObjectStorageService;
import com.guildhub.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private final ObjectStorageService storageService;

    @Value("${minio.buckets.avatars}")
    private String avatarsBucket;

    public UserController(UserService userService, ObjectStorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody AdminCreateUserDto userDto)
    {
        return ResponseEntity.ok(userService.createByAdmin(userDto));
    }

    @PutMapping("/{userId}")
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

    @PostMapping("/avatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HHmmssSSS"));
        String safeFilename = file.getOriginalFilename() == null ? "avatar" : file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
        String objectName = "users/" + username + "/" + timestamp + "-" + safeFilename;
        String url = storageService.upload(avatarsBucket, objectName, file);
        userService.updateAvatar(url);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
