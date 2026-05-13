package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.dto.ProductFilterDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.mapper.CategoryMapper;
import com.example.onlineStore.mapper.ProductMapper;
import com.example.onlineStore.service.CategoryService;
import com.example.onlineStore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    // Главная страница
    @GetMapping("/")
    public String home(Model model) {
        // Корневые категории для меню
        List<Category> rootCategories = categoryService.getRootCategories();
        List<CategoryDto> categoryDtos = categoryMapper.toDtoList(rootCategories);

        // Новинки (первые 8)
        List<Product> newProducts = productService.getNewestProducts(8);
        List<ProductDto> newProductDtos = productMapper.toDtoList(newProducts);

        // Товары со скидкой (первые 8)
        List<Product> discountedProducts = productService.getDiscountedProducts(8);
        List<ProductDto> discountedProductDtos = productMapper.toDtoList(discountedProducts);

        model.addAttribute("categories", categoryDtos);
        model.addAttribute("newProducts", newProductDtos);
        model.addAttribute("discountedProducts", discountedProductDtos);

        return "index";
    }

    // Каталог с фильтрацией и пагинацией
    @GetMapping("/catalog")
    public String catalog(ProductFilterDto filter, Model model) {
        Page<Product> productPage = productService.searchProducts(filter);
        Page<ProductDto> productDtos = productPage.map(productMapper::toDto);

        // Категории для фильтра
        List<Category> allCategories = categoryService.getAllCategories();
        List<CategoryDto> categoryDtos = categoryMapper.toDtoList(allCategories);

        model.addAttribute("products", productDtos);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("filter", filter);

        return "catalog/list";
    }

    // Категория товаров
    @GetMapping("/catalog/{slug}")
    public String category(@PathVariable String slug, ProductFilterDto filter, Model model) {
        Category category = categoryService.getCategoryBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        filter.setCategoryId(category.getId());
        Page<Product> productPage = productService.searchProducts(filter);
        Page<ProductDto> productDtos = productPage.map(productMapper::toDto);

        // Подкатегории
        List<CategoryDto> subCategories = categoryMapper.toDtoList(category.getChildren());

        // Хлебные крошки
        List<Category> breadcrumbs = getBreadcrumbs(category);

        model.addAttribute("currentCategory", categoryMapper.toDto(category));
        model.addAttribute("subCategories", subCategories);
        model.addAttribute("products", productDtos);
        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("filter", filter);

        return "catalog/list";
    }

    // Карточка товара
    @GetMapping("/product/{slug}")
    public String product(@PathVariable String slug, Model model) {
        Product product = productService.getProductBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        ProductDto productDto = productMapper.toDto(product);

        // Похожие товары (из той же категории)
        List<Product> relatedProducts = productService.getProductsByCategory(product.getCategory());
        List<ProductDto> relatedProductDtos = relatedProducts.stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(4)
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("product", productDto);
        model.addAttribute("relatedProducts", relatedProductDtos);

        return "catalog/product";
    }

    // Поиск
    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        ProductFilterDto filter = new ProductFilterDto();
        filter.setQuery(query);
        Page<Product> productPage = productService.searchProducts(filter);
        Page<ProductDto> productDtos = productPage.map(productMapper::toDto);

        model.addAttribute("products", productDtos);
        model.addAttribute("searchQuery", query);

        return "catalog/search";
    }

    // Хлебные крошки
    private List<Category> getBreadcrumbs(Category category) {
        List<Category> breadcrumbs = new ArrayList<>();
        Category current = category;
        while (current != null) {
            breadcrumbs.add(0, current);
            current = current.getParent();
        }
        return breadcrumbs;
    }
}