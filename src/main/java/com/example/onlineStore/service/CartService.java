package com.example.onlineStore.service;

import com.example.onlineStore.entity.CartItem;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    private static final String CART_SESSION_KEY = "CART_SESSION_ID";

    // Получить корзину по сессии или пользователю
    public List<CartItem> getCartItems(String sessionId, User user) {
        if (user != null) {
            return cartItemRepository.findByUser(user);
        }
        return cartItemRepository.findBySessionId(sessionId);
    }

    // Добавить товар в корзину
    @Transactional
    public CartItem addToCart(Long productId, int quantity, String sessionId, User user) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (!product.getInStock() || product.getQuantity() < quantity) {
            throw new RuntimeException("Товара нет в наличии");
        }

        CartItem existingItem = null;

        if (user != null) {
            existingItem = cartItemRepository.findByUserAndProduct(user, product).orElse(null);
        } else {
            existingItem = cartItemRepository.findBySessionIdAndProduct(sessionId, product).orElse(null);
        }

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            return cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setSessionId(sessionId);
            newItem.setUser(user);
            return cartItemRepository.save(newItem);
        }
    }

    // Обновить количество товара в корзине
    @Transactional
    public CartItem updateQuantity(Long cartItemId, int quantity, User user, String sessionId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Позиция в корзине не найдена"));

        // Проверка прав доступа
        if (user != null && item.getUser() != null && !item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Нет доступа к этой позиции");
        }
        if (user == null && item.getSessionId() != null && !item.getSessionId().equals(sessionId)) {
            throw new RuntimeException("Нет доступа к этой позиции");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(item);
            return null;
        }

        // Проверка наличия товара
        Product product = item.getProduct();
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Недостаточно товара на складе");
        }

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    // Удалить товар из корзины
    @Transactional
    public void removeFromCart(Long cartItemId, User user, String sessionId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Позиция в корзине не найдена"));

        // Проверка прав доступа
        if (user != null && item.getUser() != null && !item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Нет доступа к этой позиции");
        }
        if (user == null && item.getSessionId() != null && !item.getSessionId().equals(sessionId)) {
            throw new RuntimeException("Нет доступа к этой позиции");
        }

        cartItemRepository.delete(item);
    }

    // Очистить корзину
    @Transactional
    public void clearCart(String sessionId, User user) {
        if (user != null) {
            cartItemRepository.deleteByUser(user);
        } else {
            cartItemRepository.deleteBySessionId(sessionId);
        }
    }

    // Получить общее количество товаров в корзине
    public int getCartItemsCount(String sessionId, User user) {
        List<CartItem> items = getCartItems(sessionId, user);
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    // Получить общую сумму корзины
    public BigDecimal getCartTotal(String sessionId, User user) {
        List<CartItem> items = getCartItems(sessionId, user);
        return items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Перенести корзину из сессии в пользователя (после логина)
    @Transactional
    public void mergeCart(String sessionId, User user) {
        List<CartItem> sessionItems = cartItemRepository.findBySessionId(sessionId);

        for (CartItem sessionItem : sessionItems) {
            CartItem userItem = cartItemRepository.findByUserAndProduct(user, sessionItem.getProduct()).orElse(null);

            if (userItem != null) {
                userItem.setQuantity(userItem.getQuantity() + sessionItem.getQuantity());
                cartItemRepository.save(userItem);
                cartItemRepository.delete(sessionItem);
            } else {
                sessionItem.setUser(user);
                sessionItem.setSessionId(null);
                cartItemRepository.save(sessionItem);
            }
        }
    }

    // Получить позицию корзины по ID
    public String getOrCreateSessionId(HttpSession session) {
        String sessionId = (String) session.getAttribute(CART_SESSION_KEY);
        if (sessionId == null) {
            sessionId = java.util.UUID.randomUUID().toString();
            session.setAttribute(CART_SESSION_KEY, sessionId);
        }
        return sessionId;
    }
}