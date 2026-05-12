package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.CartItemDto;
import com.example.onlineStore.entity.CartItem;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {

    // Entity → DTO
    public CartItemDto toDto(CartItem entity) {
        if (entity == null) return null;

        CartItemDto dto = new CartItemDto();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());

        if (entity.getProduct() != null) {
            Product product = entity.getProduct();
            dto.setProductId(product.getId());
            dto.setProductName(product.getName());
            dto.setProductSlug(product.getSlug());
            dto.setProductImage(product.getImagePath());
            dto.setProductPrice(product.getPrice());
            dto.setMaxAvailable(product.getQuantity());
        }

        return dto;
    }

    // List<Entity> → List<DTO>
    public List<CartItemDto> toDtoList(List<CartItem> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // DTO → Entity (для добавления в корзину)
    public CartItem toEntity(Product product, Integer quantity, String sessionId, User user) {
        CartItem entity = new CartItem();
        entity.setProduct(product);
        entity.setQuantity(quantity != null ? quantity : 1);
        entity.setSessionId(sessionId);
        entity.setUser(user);
        return entity;
    }
}