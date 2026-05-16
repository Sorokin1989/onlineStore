//package com.example.onlineStore.controller;
//
//import com.example.onlineStore.dto.ProductDto;
//import com.example.onlineStore.dto.ProductFilterDto;
//import com.example.onlineStore.entity.Product;
//import com.example.onlineStore.mapper.ProductMapper;
//import com.example.onlineStore.service.ProductService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequestMapping("/search")
//@RequiredArgsConstructor
//public class SearchController {
//
//    private final ProductService productService;
//    private final ProductMapper productMapper;
//
//    @GetMapping
//    public String search(@RequestParam String q,
//                         @RequestParam(defaultValue = "0") int page,
//                         Model model) {
//        ProductFilterDto filter = new ProductFilterDto();
//        filter.setQuery(q);
//        filter.setPage(page);
//        filter.setSize(12);
//
//        Page<Product> productPage = productService.searchProducts(filter);
//        Page<ProductDto> productDtos = productPage.map(productMapper::toDto);
//
//        model.addAttribute("products", productDtos);
//        model.addAttribute("searchQuery", q);
//        model.addAttribute("totalResults", productPage.getTotalElements());
//        model.addAttribute("content", "pages/user/search/results :: content");
//
//        return "layouts/main";
//    }
//}