package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CompatibilityDto;
import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.dto.ProductFilterDto;
import com.example.onlineStore.entity.Compatibility;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.mapper.CompatibilityMapper;
import com.example.onlineStore.mapper.ProductMapper;
import com.example.onlineStore.service.CompatibilityService;
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
@RequestMapping("/compatible")
@RequiredArgsConstructor
public class CompatibilityController {

    private final CompatibilityService compatibilityService;
    private final ProductService productService;
    private final CompatibilityMapper compatibilityMapper;
    private final ProductMapper productMapper;

    // Все устройства для совместимости
    @GetMapping
    public String allCompatibilities(Model model) {
        List<Compatibility> compatibilities = compatibilityService.getActiveCompatibilities();
        model.addAttribute("compatibilities", compatibilityMapper.toDtoList(compatibilities));
        model.addAttribute("content", "pages/user/compatible/list :: content");
        return "layouts/main";
    }

    // Товары, совместимые с устройством
    @GetMapping("/{name}")
    public String compatibleProducts(@PathVariable String name, ProductFilterDto filter, Model model) {
        Compatibility compatibility = compatibilityService.getCompatibilityByName(name)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено"));

        filter.setCompatibilityIds(List.of(compatibility.getId()));
        Page<Product> productPage = productService.searchProducts(filter);
        Page<ProductDto> productDtos = productPage.map(productMapper::toDto);

        model.addAttribute("compatibility", compatibilityMapper.toDto(compatibility));
        model.addAttribute("products", productDtos);
        model.addAttribute("filter", filter);
        model.addAttribute("content", "pages/user/compatible/products :: content");

        return "layouts/main";
    }
}