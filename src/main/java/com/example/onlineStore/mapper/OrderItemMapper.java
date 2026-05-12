package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.OrderItemDto;
import com.example.onlineStore.entity.OrderItem;
import com.example.onlineStore.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {

    // Entity → DTO
    public OrderItemDto toDto(OrderItem entity) {
        if (entity == null) return null;

        OrderItemDto dto = new OrderItemDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct() != null ? entity.getProduct().getId() : null);
        dto.setProductName(entity.getProductName());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setTotal(entity.getTotal());

        // Дополнительные поля для отображения
        if (entity.getProduct() != null) {
            dto.setProductSlug(entity.getProduct().getSlug());
            dto.setProductImage(entity.getProduct().getImagePath());
        }

        return dto;
    }

    // List<Entity> → List<DTO>
    public List<OrderItemDto> toDtoList(List<OrderItem> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // DTO → Entity (для создания позиции заказа из корзины)
    public OrderItem toEntity(Product product, Integer quantity) {
        OrderItem entity = new OrderItem();
        entity.setProduct(product);
        entity.setProductName(product.getName());
        entity.setPrice(product.getPrice());
        entity.setQuantity(quantity);
        entity.setTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return entity;
    }
}