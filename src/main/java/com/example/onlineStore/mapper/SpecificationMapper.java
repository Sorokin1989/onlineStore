package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.SpecificationDto;
import com.example.onlineStore.entity.Specification;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecificationMapper {

    public SpecificationDto toDto(Specification entity) {
        if (entity == null) return null;

        SpecificationDto dto = new SpecificationDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUnit(entity.getUnit());
        dto.setFilterable(entity.getFilterable());
        dto.setActive(entity.getActive());

        return dto;
    }

    public List<SpecificationDto> toDtoList(List<Specification> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Specification toEntity(SpecificationDto dto) {
        if (dto == null) return null;

        Specification entity = new Specification();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setUnit(dto.getUnit());
        entity.setFilterable(dto.getFilterable());
        entity.setActive(dto.getActive());

        return entity;
    }
}