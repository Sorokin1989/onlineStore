package com.example.onlineStore.config;

import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.UserRole;
import com.example.onlineStore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Создаём админа, если его нет
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Администратор");
            admin.setRole(UserRole.ROLE_ADMIN);
            admin.setCreatedAt(java.time.LocalDateTime.now());
            userRepository.save(admin);
            System.out.println("Администратор создан: admin@example.com / admin123");
        }
    }
}