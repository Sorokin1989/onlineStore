package com.example.onlineStore.service;

import com.example.onlineStore.entity.Brand;
import com.example.onlineStore.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Brand> getActiveBrands() {
        return brandRepository.findByActiveTrueOrderByNameAsc();
    }

    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    public Optional<Brand> getBrandBySlug(String slug) {
        return brandRepository.findBySlug(slug);
    }

    public Optional<Brand> getBrandByName(String name) {
        return brandRepository.findByName(name);
    }

    @Transactional
    public Brand createBrand(Brand brand) {
        if (brand.getSlug() == null || brand.getSlug().isEmpty()) {
            brand.setSlug(transliterate(brand.getName()));
        }
        return brandRepository.save(brand);
    }

    @Transactional
    public Brand updateBrand(Long id, Brand updatedBrand) {
        Brand existing = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бренд не найден"));

        existing.setName(updatedBrand.getName());
        existing.setSlug(updatedBrand.getSlug());
        existing.setLogo(updatedBrand.getLogo());
        existing.setDescription(updatedBrand.getDescription());
        existing.setActive(updatedBrand.getActive());

        return brandRepository.save(existing);
    }

    @Transactional
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }

    private String transliterate(String input) {
        return input.toLowerCase()
                .replace(" ", "-")
                .replace("а", "a").replace("б", "b").replace("в", "v")
                .replace("г", "g").replace("д", "d").replace("е", "e")
                .replace("ё", "e").replace("ж", "zh").replace("з", "z")
                .replace("и", "i").replace("й", "y").replace("к", "k")
                .replace("л", "l").replace("м", "m").replace("н", "n")
                .replace("о", "o").replace("п", "p").replace("р", "r")
                .replace("с", "s").replace("т", "t").replace("у", "u")
                .replace("ф", "f").replace("х", "kh").replace("ц", "ts")
                .replace("ч", "ch").replace("ш", "sh").replace("щ", "sch")
                .replace("ъ", "").replace("ы", "y").replace("ь", "")
                .replace("э", "e").replace("ю", "yu").replace("я", "ya")
                .replaceAll("[^a-z0-9-]", "");
    }
}