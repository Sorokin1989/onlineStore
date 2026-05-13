package com.example.onlineStore.service;

import com.example.onlineStore.entity.ProductImage;
import com.example.onlineStore.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public List<ProductImage> getImagesByProductId(Long productId) {
        return productImageRepository.findByProductIdOrderBySortOrderAsc(productId);
    }

    public List<ProductImage> getMainImageByProductId(Long productId) {
        return productImageRepository.findByProductIdAndIsMainTrue(productId);
    }

    @Transactional
    public ProductImage createImage(ProductImage image) {
        return productImageRepository.save(image);
    }

    @Transactional
    public void deleteImage(Long id) {
        productImageRepository.deleteById(id);
    }

    @Transactional
    public void deleteByProductId(Long productId) {
        productImageRepository.deleteByProductId(productId);
    }

    @Transactional
    public void setMainImage(Long productId, Long imageId) {
        // Сбрасываем isMain для всех изображений товара
        List<ProductImage> images = productImageRepository.findByProductIdOrderBySortOrderAsc(productId);
        for (ProductImage image : images) {
            image.setIsMain(image.getId().equals(imageId));
            productImageRepository.save(image);
        }
    }
}