package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.BrandDto;
import com.example.onlineStore.entity.Brand;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {

    public BrandDto toDto(Brand entity) {
        if (entity == null) return null;

        BrandDto dto = new BrandDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setLogo(entity.getLogo());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.getActive());

        return dto;
    }

    public List<BrandDto> toDtoList(List<Brand> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Brand toEntity(BrandDto dto) {
        if (dto == null) return null;

        Brand entity = new Brand();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        entity.setLogo(dto.getLogo());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.getActive());

        return entity;
    }

    public void updateEntity(BrandDto dto, Brand entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getSlug() != null) entity.setSlug(dto.getSlug());
        if (dto.getLogo() != null) entity.setLogo(dto.getLogo());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
    }
}