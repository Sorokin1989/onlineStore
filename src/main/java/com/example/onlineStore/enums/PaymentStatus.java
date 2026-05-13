package com.example.onlineStore.enums;

public enum PaymentStatus {
    CREATED,      // Создан, ожидает оплаты
    PENDING,      // В обработке
    SUCCEEDED,    // Успешно оплачен
    CANCELLED,    // Отменён
    REFUNDED,     // Возвращён
    EXPIRED       // Истёк
}