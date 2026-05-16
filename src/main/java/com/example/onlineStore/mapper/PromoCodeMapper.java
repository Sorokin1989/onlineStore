package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.PromoCodeDto;
import com.example.onlineStore.entity.PromoCode;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PromoCodeMapper {

    public PromoCodeDto toDto(PromoCode entity) {
        if (entity == null) return null;

        PromoCodeDto dto = new PromoCodeDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDiscountPercent(entity.getDiscountPercent());
        dto.setDiscountAmount(entity.getDiscountAmount());
        dto.setMinOrderAmount(entity.getMinOrderAmount());
        dto.setMaxDiscount(entity.getMaxDiscount());
        dto.setUsageLimit(entity.getUsageLimit());
        dto.setUsedCount(entity.getUsedCount());
        dto.setValidFrom(entity.getValidFrom());
        dto.setValidTo(entity.getValidTo());
        dto.setActive(entity.getActive());

        return dto;
    }

    public List<PromoCodeDto> toDtoList(List<PromoCode> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PromoCode toEntity(PromoCodeDto dto) {
        if (dto == null) return null;

        PromoCode entity = new PromoCode();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDiscountPercent(dto.getDiscountPercent());
        entity.setDiscountAmount(dto.getDiscountAmount());
        entity.setMinOrderAmount(dto.getMinOrderAmount());
        entity.setMaxDiscount(dto.getMaxDiscount());
        entity.setUsageLimit(dto.getUsageLimit());
        entity.setUsedCount(dto.getUsedCount() != null ? dto.getUsedCount() : 0);
        entity.setValidFrom(dto.getValidFrom());
        entity.setValidTo(dto.getValidTo());
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);

        return entity;
    }

    public void updateEntity(PromoCodeDto dto, PromoCode entity) {
        if (dto.getCode() != null) entity.setCode(dto.getCode());
        if (dto.getDiscountPercent() != null) entity.setDiscountPercent(dto.getDiscountPercent());
        if (dto.getDiscountAmount() != null) entity.setDiscountAmount(dto.getDiscountAmount());
        if (dto.getMinOrderAmount() != null) entity.setMinOrderAmount(dto.getMinOrderAmount());
        if (dto.getMaxDiscount() != null) entity.setMaxDiscount(dto.getMaxDiscount());
        if (dto.getUsageLimit() != null) entity.setUsageLimit(dto.getUsageLimit());
        if (dto.getUsedCount() != null) entity.setUsedCount(dto.getUsedCount());
        if (dto.getValidFrom() != null) entity.setValidFrom(dto.getValidFrom());
        if (dto.getValidTo() != null) entity.setValidTo(dto.getValidTo());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
    }
}