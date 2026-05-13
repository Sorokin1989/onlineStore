package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.CompatibilityDto;
import com.example.onlineStore.entity.Compatibility;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompatibilityMapper {

    public CompatibilityDto toDto(Compatibility entity) {
        if (entity == null) return null;

        CompatibilityDto dto = new CompatibilityDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBrand(entity.getBrand());
        dto.setActive(entity.getActive());

        return dto;
    }

    public List<CompatibilityDto> toDtoList(List<Compatibility> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Compatibility toEntity(CompatibilityDto dto) {
        if (dto == null) return null;

        Compatibility entity = new Compatibility();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setBrand(dto.getBrand());
        entity.setActive(dto.getActive());

        return entity;
    }
}