package com.example.onlineStore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank(message = "Укажите ваше имя")
    private String fullName;

    @NotBlank(message = "Укажите email")
    @Email(message = "Неверный формат email")
    private String email;

    @NotBlank(message = "Укажите телефон")
    private String phone;

    @NotBlank(message = "Укажите адрес доставки")
    private String address;

    private String comment;

    @NotBlank(message = "Выберите способ оплаты")
    private String paymentMethod;
}