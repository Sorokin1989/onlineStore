package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.TagDto;
import com.example.onlineStore.entity.Tag;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public TagDto toDto(Tag entity) {
        if (entity == null) return null;

        TagDto dto = new TagDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setActive(entity.getActive());

        return dto;
    }

    public List<TagDto> toDtoList(List<Tag> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Tag toEntity(TagDto dto) {
        if (dto == null) return null;

        Tag entity = new Tag();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        entity.setActive(dto.getActive());

        return entity;
    }
}