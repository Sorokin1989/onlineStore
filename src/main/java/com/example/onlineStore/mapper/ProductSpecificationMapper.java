package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.ProductSpecificationDto;
import com.example.onlineStore.entity.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductSpecificationMapper {

    private final SpecificationMapper specificationMapper;

    public ProductSpecificationDto toDto(ProductSpecification entity) {
        if (entity == null) return null;

        ProductSpecificationDto dto = new ProductSpecificationDto();
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());

        if (entity.getSpecification() != null) {
            dto.setSpecificationName(entity.getSpecification().getName());
            dto.setSpecificationUnit(entity.getSpecification().getUnit());
        }

        return dto;
    }

    public List<ProductSpecificationDto> toDtoList(List<ProductSpecification> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}