package com.example.onlineStore.repository;

import com.example.onlineStore.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    Optional<PromoCode> findByCode(String code);

    List<PromoCode> findByActiveTrue();

    List<PromoCode> findByValidFromBeforeAndValidToAfter(LocalDateTime now, LocalDateTime now2);

    boolean existsByCode(String code);
}