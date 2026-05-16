package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.PageDto;
import com.example.onlineStore.entity.Page;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PageMapper {

    public PageDto toDto(Page entity) {
        if (entity == null) return null;

        PageDto dto = new PageDto();
        dto.setId(entity.getId());
        dto.setSlug(entity.getSlug());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setMetaTitle(entity.getMetaTitle());
        dto.setMetaDescription(entity.getMetaDescription());
        dto.setSortOrder(entity.getSortOrder());
        dto.setActive(entity.getActive());

        return dto;
    }

    public List<PageDto> toDtoList(List<Page> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Page toEntity(PageDto dto) {
        if (dto == null) return null;

        Page entity = new Page();
        entity.setId(dto.getId());
        entity.setSlug(dto.getSlug());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setMetaTitle(dto.getMetaTitle());
        entity.setMetaDescription(dto.getMetaDescription());
        entity.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);

        return entity;
    }

    public void updateEntity(PageDto dto, Page entity) {
        if (dto.getSlug() != null) entity.setSlug(dto.getSlug());
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getContent() != null) entity.setContent(dto.getContent());
        if (dto.getMetaTitle() != null) entity.setMetaTitle(dto.getMetaTitle());
        if (dto.getMetaDescription() != null) entity.setMetaDescription(dto.getMetaDescription());
        if (dto.getSortOrder() != null) entity.setSortOrder(dto.getSortOrder());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
    }
}