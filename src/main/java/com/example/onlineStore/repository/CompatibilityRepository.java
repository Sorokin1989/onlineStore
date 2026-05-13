package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Compatibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompatibilityRepository extends JpaRepository<Compatibility, Long> {

    Optional<Compatibility> findByName(String name);

    List<Compatibility> findByActiveTrueOrderByNameAsc();

    List<Compatibility> findByBrand(String brand);
}