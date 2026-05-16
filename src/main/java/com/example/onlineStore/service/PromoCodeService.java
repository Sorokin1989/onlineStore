package com.example.onlineStore.service;

import com.example.onlineStore.entity.PromoCode;
import com.example.onlineStore.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    public List<PromoCode> getActivePromoCodes() {
        return promoCodeRepository.findByActiveTrue();
    }

    public Optional<PromoCode> getPromoCodeById(Long id) {
        return promoCodeRepository.findById(id);
    }

    public Optional<PromoCode> getPromoCodeByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }

    @Transactional
    public PromoCode createPromoCode(PromoCode promoCode) {
        if (promoCodeRepository.existsByCode(promoCode.getCode())) {
            throw new RuntimeException("Промокод с таким кодом уже существует");
        }
        return promoCodeRepository.save(promoCode);
    }

    @Transactional
    public PromoCode updatePromoCode(Long id, PromoCode updated) {
        PromoCode existing = promoCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Промокод не найден"));

        existing.setCode(updated.getCode());
        existing.setDiscountPercent(updated.getDiscountPercent());
        existing.setDiscountAmount(updated.getDiscountAmount());
        existing.setMinOrderAmount(updated.getMinOrderAmount());
        existing.setMaxDiscount(updated.getMaxDiscount());
        existing.setUsageLimit(updated.getUsageLimit());
        existing.setValidFrom(updated.getValidFrom());
        existing.setValidTo(updated.getValidTo());
        existing.setActive(updated.getActive());

        return promoCodeRepository.save(existing);
    }

    @Transactional
    public void deletePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }

    @Transactional
    public void incrementUsedCount(Long id) {
        PromoCode promoCode = promoCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Промокод не найден"));
        promoCode.setUsedCount(promoCode.getUsedCount() + 1);
        promoCodeRepository.save(promoCode);
    }

    public boolean isValidPromoCode(String code, BigDecimal orderAmount) {
        Optional<PromoCode> optional = promoCodeRepository.findByCode(code);
        if (optional.isEmpty()) return false;

        PromoCode promoCode = optional.get();
        return promoCode.isValid() && orderAmount.compareTo(promoCode.getMinOrderAmount()) >= 0;
    }

    public BigDecimal calculateDiscount(PromoCode promoCode, BigDecimal orderAmount) {
        BigDecimal discount = BigDecimal.ZERO;

        if (promoCode.getDiscountPercent() != null && promoCode.getDiscountPercent() > 0) {
            discount = orderAmount.multiply(BigDecimal.valueOf(promoCode.getDiscountPercent()))
                    .divide(BigDecimal.valueOf(100));
        } else if (promoCode.getDiscountAmount() != null && promoCode.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            discount = promoCode.getDiscountAmount();
        }

        if (promoCode.getMaxDiscount() != null && discount.compareTo(promoCode.getMaxDiscount()) > 0) {
            discount = promoCode.getMaxDiscount();
        }

        return discount;
    }
}