package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.WarrantyDto;
import com.example.onlineStore.entity.Warranty;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarrantyMapper {

    public WarrantyDto toDto(Warranty entity) {
        if (entity == null) return null;

        WarrantyDto dto = new WarrantyDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDurationMonths(entity.getDurationMonths());
        dto.setDescription(entity.getDescription());
        dto.setAdditionalPrice(entity.getAdditionalPrice());
        dto.setActive(entity.getActive());

        return dto;
    }

    public List<WarrantyDto> toDtoList(List<Warranty> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Warranty toEntity(WarrantyDto dto) {
        if (dto == null) return null;

        Warranty entity = new Warranty();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDurationMonths(dto.getDurationMonths());
        entity.setDescription(dto.getDescription());
        entity.setAdditionalPrice(dto.getAdditionalPrice());
        entity.setActive(dto.getActive());

        return entity;
    }
}