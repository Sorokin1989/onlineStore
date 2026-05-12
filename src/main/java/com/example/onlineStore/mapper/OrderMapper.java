package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.dto.OrderItemDto;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.OrderItem;
import com.example.onlineStore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    // Entity → DTO
    public OrderDto toDto(Order entity) {
        if (entity == null) return null;

        OrderDto dto = new OrderDto();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setComment(entity.getComment());
        dto.setTotal(entity.getTotal());
        dto.setDeliveryCost(entity.getDeliveryCost());
        dto.setStatus(entity.getStatus());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setPaymentId(entity.getPaymentId());
        dto.setCreatedAt(entity.getCreatedAt());

        // Подсчёт subtotal (сумма товаров без доставки)
        if (entity.getItems() != null) {
            BigDecimal subtotal = entity.getItems().stream()
                    .map(OrderItem::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setSubtotal(subtotal);
        }

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserEmail(entity.getUser().getEmail());
        }

        if (entity.getItems() != null) {
            dto.setItems(orderItemMapper.toDtoList(entity.getItems()));
        }

        return dto;
    }

    // List<Entity> → List<DTO>
    public List<OrderDto> toDtoList(List<Order> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // DTO → Entity (для создания заказа из корзины)
    public Order toEntity(OrderDto dto, User user, List<OrderItem> items) {
        Order entity = new Order();
        entity.setOrderNumber(generateOrderNumber());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setComment(dto.getComment());
        entity.setTotal(dto.getTotal());
        entity.setDeliveryCost(dto.getDeliveryCost() != null ? dto.getDeliveryCost() : BigDecimal.ZERO);
        entity.setStatus("NEW");
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setUser(user);
        entity.setCreatedAt(LocalDateTime.now());

        if (items != null) {
            for (OrderItem item : items) {
                entity.addItem(item);
            }
        }

        return entity;
    }

    // Генерация уникального номера заказа
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}