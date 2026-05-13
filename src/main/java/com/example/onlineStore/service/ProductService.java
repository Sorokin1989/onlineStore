package com.example.onlineStore.service;

import com.example.onlineStore.dto.ProductFilterDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    // Получить все товары
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Получить товары по категории
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategoryAndActiveTrueOrderByCreatedAtDesc(category);
    }

    // Получить товар по Slug
    public Optional<Product> getProductBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    // Получить товар по ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Получить новинки (последние N товаров)
    public List<Product> getNewestProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return productRepository.findAllByActiveTrue(pageable).getContent();
    }

    // Получить товары со скидкой
    public List<Product> getDiscountedProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findByOldPriceIsNotNullAndActiveTrue(pageable);
    }

    // Поиск товаров с фильтрацией и пагинацией
    public Page<Product> searchProducts(ProductFilterDto filter) {
        Sort sort = Sort.by(Sort.Direction.fromString(filter.getSortDirection()), filter.getSortField());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        Category category = null;
        if (filter.hasCategory()) {
            category = categoryService.getCategoryById(filter.getCategoryId()).orElse(null);
        }

        return productRepository.searchProducts(
                filter.getQuery(),
                category,
                filter.getMinPrice(),
                filter.getMaxPrice(),
                filter.getInStock(),
                pageable
        );
    }

    // Создать товар
    @Transactional
    public Product createProduct(Product product) {
        if (product.getSlug() == null || product.getSlug().isEmpty()) {
            product.setSlug(transliterate(product.getName()) + "-" + System.currentTimeMillis());
        }
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    // Обновить товар
    @Transactional
    public Product updateProduct(Long id, Product updated) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        existing.setName(updated.getName());
        existing.setSlug(updated.getSlug());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setOldPrice(updated.getOldPrice());
        existing.setImagePath(updated.getImagePath());
        existing.setAdditionalImages(updated.getAdditionalImages());
        existing.setInStock(updated.getInStock());
        existing.setQuantity(updated.getQuantity());
        existing.setCategory(updated.getCategory());
        existing.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(existing);
    }

    // Обновить остаток на складе
    @Transactional
    public void updateStock(Long id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        product.setQuantity(quantity);
        product.setInStock(quantity > 0);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    // Удалить товар
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Проверка наличия товара
    public boolean isInStock(Long id, int requestedQuantity) {
        Optional<Product> product = productRepository.findById(id);
        return product.isPresent() && product.get().getInStock() && product.get().getQuantity() >= requestedQuantity;
    }

    // Уменьшение остатка при заказе
    @Transactional
    public void decreaseStock(Long id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        int newQuantity = product.getQuantity() - quantity;
        if (newQuantity < 0) {
            throw new RuntimeException("Недостаточно товара на складе");
        }

        product.setQuantity(newQuantity);
        product.setInStock(newQuantity > 0);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    private String transliterate(String input) {
        return input.toLowerCase()
                .replace("а", "a").replace("б", "b").replace("в", "v").replace("г", "g")
                .replace("д", "d").replace("е", "e").replace("ё", "e").replace("ж", "zh")
                .replace("з", "z").replace("и", "i").replace("й", "y").replace("к", "k")
                .replace("л", "l").replace("м", "m").replace("н", "n").replace("о", "o")
                .replace("п", "p").replace("р", "r").replace("с", "s").replace("т", "t")
                .replace("у", "u").replace("ф", "f").replace("х", "kh").replace("ц", "ts")
                .replace("ч", "ch").replace("ш", "sh").replace("щ", "sch").replace("ъ", "")
                .replace("ы", "y").replace("ь", "").replace("э", "e").replace("ю", "yu")
                .replace("я", "ya").replace(" ", "-")
                .replaceAll("[^a-z0-9-]", "");
    }
}