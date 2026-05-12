package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Entity → DTO (без пароля)
    public UserDto toDto(User entity) {
        if (entity == null) return null;

        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFullName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

    // List<Entity> → List<DTO>
    public List<UserDto> toDtoList(List<User> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // DTO → Entity (для регистрации)
    public User toEntity(UserDto dto) {
        User entity = new User();
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setRole("ROLE_USER");
        return entity;
    }

    // DTO → Entity (для обновления профиля)
    public void updateEntity(UserDto dto, User entity) {
        if (dto.getFullName() != null) entity.setFullName(dto.getFullName());
        if (dto.getPhone() != null) entity.setPhone(dto.getPhone());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }
}