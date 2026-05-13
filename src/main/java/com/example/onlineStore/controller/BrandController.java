package com.example.onlineStore.controller;

import com.example.onlineStore.dto.BrandDto;
import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.dto.ProductFilterDto;
import com.example.onlineStore.entity.Brand;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.mapper.BrandMapper;
import com.example.onlineStore.mapper.ProductMapper;
import com.example.onlineStore.service.BrandService;
import com.example.onlineStore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final ProductService productService;
    private final BrandMapper brandMapper;
    private final ProductMapper productMapper;

    // Список всех брендов
    @GetMapping
    public String allBrands(Model model) {
        List<Brand> brands = brandService.getActiveBrands();
        model.addAttribute("brands", brandMapper.toDtoList(brands));
        return "brands/list";
    }

    // Товары бренда
    @GetMapping("/{slug}")
    public String brandProducts(@PathVariable String slug, ProductFilterDto filter, Model model) {
        Brand brand = brandService.getBrandBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Бренд не найден"));

        filter.setBrandIds(List.of(brand.getId()));
        Page<Product> productPage = productService.searchProducts(filter);
        Page<ProductDto> productDtos = productPage.map(productMapper::toDto);

        model.addAttribute("brand", brandMapper.toDto(brand));
        model.addAttribute("products", productDtos);
        model.addAttribute("filter", filter);

        return "brands/products";
    }
}