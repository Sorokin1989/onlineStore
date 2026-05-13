package com.example.onlineStore.entity;

import com.example.onlineStore.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String address;

    private String comment;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryCost = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    // Вспомогательные методы
    public boolean isPaid() {
        return status == OrderStatus.PAID;
    }

    public boolean isNew() {
        return status == OrderStatus.NEW;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }
}