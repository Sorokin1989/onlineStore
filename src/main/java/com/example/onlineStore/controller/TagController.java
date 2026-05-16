package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.dto.ProductFilterDto;
import com.example.onlineStore.dto.TagDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.Tag;
import com.example.onlineStore.mapper.ProductMapper;
import com.example.onlineStore.mapper.TagMapper;
import com.example.onlineStore.service.ProductService;
import com.example.onlineStore.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final ProductService productService;
    private final TagMapper tagMapper;
    private final ProductMapper productMapper;

    @GetMapping
    public String allTags(Model model) {
        List<Tag> tags = tagService.getActiveTags();
        model.addAttribute("tags", tagMapper.toDtoList(tags));
        model.addAttribute("content", "pages/user/tags/list :: content");
        return "layouts/main";
    }

    @GetMapping("/{slug}")
    public String tagProducts(@PathVariable String slug, ProductFilterDto filter, Model model) {
        Tag tag = tagService.getTagBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));

        filter.setTagIds(List.of(tag.getId()));
        Page<Product> productPage = productService.searchProducts(filter);
        Page<ProductDto> productDtos = productPage.map(productMapper::toDto);

        model.addAttribute("tag", tagMapper.toDto(tag));
        model.addAttribute("products", productDtos);
        model.addAttribute("filter", filter);
        model.addAttribute("content", "pages/user/tags/products :: content");
        return "layouts/main";
    }
}