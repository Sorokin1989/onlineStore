package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.ColorDto;
import com.example.onlineStore.entity.Color;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ColorMapper {

    public ColorDto toDto(Color entity) {
        if (entity == null) return null;

        ColorDto dto = new ColorDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setHex(entity.getHex());
        dto.setActive(entity.getActive());

        return dto;
    }

    public List<ColorDto> toDtoList(List<Color> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Color toEntity(ColorDto dto) {
        if (dto == null) return null;

        Color entity = new Color();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setHex(dto.getHex());
        entity.setActive(dto.getActive());

        return entity;
    }
}