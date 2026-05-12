package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private String orderNumber;

    // Данные покупателя
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String comment;

    // Финансы
    private BigDecimal subtotal;      // сумма товаров
    private BigDecimal deliveryCost;
    private BigDecimal total;

    // Статусы: NEW, PAID, PROCESSING, DELIVERED, CANCELLED
    private String status;
    private String paymentMethod;     // CARD, CASH, ONLINE
    private String paymentId;

    private Long userId;
    private String userEmail;

    private LocalDateTime createdAt;

    private List<OrderItemDto> items = new ArrayList<>();

    // Вспомогательные методы
    public String getStatusDisplay() {
        switch (status) {
            case "NEW": return "Новый";
            case "PAID": return "Оплачен";
            case "PROCESSING": return "В обработке";
            case "DELIVERED": return "Доставлен";
            case "CANCELLED": return "Отменён";
            default: return status;
        }
    }

    public String getPaymentMethodDisplay() {
        switch (paymentMethod) {
            case "CARD": return "Банковская карта онлайн";
            case "CASH": return "Наличными при получении";
            case "ONLINE": return "Онлайн-оплата";
            default: return paymentMethod;
        }
    }

    public boolean isPaid() {
        return "PAID".equals(status);
    }

    public boolean isNew() {
        return "NEW".equals(status);
    }

    public String getFormattedTotal() {
        return String.format("%,.2f ₽", total).replace(",", " ");
    }
}