package com.example.onlineStore.service;

import com.example.onlineStore.entity.CartItem;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.OrderItem;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    // Получить заказы пользователя
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // Получить заказ по номеру
    public Optional<Order> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    // Получить заказ по ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Получить все заказы (для админа)
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    // Создать заказ из корзины
    @Transactional
    public Order createOrder(User user, String sessionId, String fullName, String email,
                             String phone, String address, String comment, String paymentMethod) {

        // Получаем корзину
        List<CartItem> cartItems = cartService.getCartItems(sessionId, user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Корзина пуста");
        }

        // Создаём заказ
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setFullName(fullName);
        order.setEmail(email);
        order.setPhone(phone);
        order.setAddress(address);
        order.setComment(comment);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("NEW");
        order.setCreatedAt(LocalDateTime.now());

        // Вычисляем сумму и создаём позиции заказа
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            // Проверка наличия
            if (!product.getInStock() || product.getQuantity() < quantity) {
                throw new RuntimeException("Товар \"" + product.getName() + "\" недоступен в нужном количестве");
            }

            // Создаём позицию заказа
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(quantity);
            orderItem.setTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            orderItems.add(orderItem);
            total = total.add(orderItem.getTotal());

            // Уменьшаем остаток на складе
            productService.decreaseStock(product.getId(), quantity);
        }

        order.setTotal(total);
        order.setDeliveryCost(BigDecimal.ZERO); // Можно рассчитать отдельно

        for (OrderItem item : orderItems) {
            order.addItem(item);
        }

        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);

        // Очищаем корзину
        cartService.clearCart(sessionId, user);

        return savedOrder;
    }

    // Обновить статус заказа
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Получить заказы по статусу
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    // Генерация уникального номера заказа
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" +
                java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}