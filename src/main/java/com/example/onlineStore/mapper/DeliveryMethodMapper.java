package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.DeliveryMethodDto;
import com.example.onlineStore.entity.DeliveryMethod;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeliveryMethodMapper {

    public DeliveryMethodDto toDto(DeliveryMethod entity) {
        if (entity == null) return null;
        DeliveryMethodDto dto = new DeliveryMethodDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setEstimatedDays(entity.getEstimatedDays());
        dto.setActive(entity.getActive());
        return dto;
    }

    public List<DeliveryMethodDto> toDtoList(List<DeliveryMethod> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public DeliveryMethod toEntity(DeliveryMethodDto dto) {
        if (dto == null) return null;
        DeliveryMethod entity = new DeliveryMethod();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setEstimatedDays(dto.getEstimatedDays());
        entity.setActive(dto.getActive());
        return entity;
    }
}