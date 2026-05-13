package com.example.onlineStore.service;

import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.UserRole;
import com.example.onlineStore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Загрузка пользователя по email (для Spring Security)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + email));
    }

    // Получить пользователя по ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Получить пользователя по email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Регистрация нового пользователя
    @Transactional
    public User registerUser(String email, String password, String fullName, String phone) {
        // Проверка на существующего пользователя
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setRole(UserRole.valueOf("ROLE_USER"));
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // Обновление профиля пользователя
    @Transactional
    public User updateProfile(Long userId, String fullName, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (fullName != null) user.setFullName(fullName);
        if (phone != null) user.setPhone(phone);

        return userRepository.save(user);
    }

    // Смена пароля
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Неверный старый пароль");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Создать администратора (для инициализации)
    @Transactional
    public User createAdmin(String email, String password, String fullName) {
        if (userRepository.findByEmail(email).isPresent()) {
            return null;
        }

        User admin = new User();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFullName(fullName);
        admin.setRole(UserRole.valueOf("ROLE_ADMIN"));
        admin.setCreatedAt(LocalDateTime.now());

        return userRepository.save(admin);
    }

    // Проверка, является ли пользователь администратором
    public boolean isAdmin(User user) {
        return user != null && "ROLE_ADMIN".equals(user.getRole());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();

    }

    public void changeUserRole(Long id, String role) {
    }

    public void toggleUserBlock(Long id) {
    }
}