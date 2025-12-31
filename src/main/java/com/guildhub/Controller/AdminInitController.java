package com.guildhub.Controller;

import com.guildhub.Entity.Role.UserRole;
import com.guildhub.Entity.User;
import com.guildhub.Reposirtory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
@RequestMapping("/api/admin-init")
public class AdminInitController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Создает первого админа, если в системе еще нет пользователей с ролью ADMIN.
     * 
     * @param username Имя пользователя (по умолчанию: admin)
     * @param password Пароль (по умолчанию: admin123)
     * @param email Email (по умолчанию: admin@guildhub.com)
     * @return Сообщение об успехе или ошибке
     */
    @PostMapping("/create-first-admin")
    public ResponseEntity<?> createFirstAdmin(
            @RequestParam(required = false, defaultValue = "admin") String username,
            @RequestParam(required = false, defaultValue = "admin123") String password,
            @RequestParam(required = false, defaultValue = "admin@guildhub.com") String email) {

        boolean hasAdmin = userRepository.findAll().stream()
                .anyMatch(user -> user.getRoles().contains(UserRole.ADMIN));
        
        if (hasAdmin) {
            return ResponseEntity.badRequest()
                    .body("Админ уже существует в системе. Используйте админ панель для создания новых админов.");
        }
        
        // Проверяем, существует ли пользователь с таким username или email
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Пользователь с таким username уже существует.");
        }
        
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Пользователь с таким email уже существует.");
        }
        
        User admin = new User();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRoles(Collections.singletonList(UserRole.ADMIN));
        admin.setAvatarUrl("https://example.com/default-avatar.png");
        
        userRepository.save(admin);
        
        return ResponseEntity.ok("Админ успешно создан! Username: " + username + 
                ". ВАЖНО: Отключите этот endpoint в production!");
    }
}

