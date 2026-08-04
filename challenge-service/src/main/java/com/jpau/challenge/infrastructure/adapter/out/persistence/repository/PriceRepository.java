package com.jpau.challenge.infrastructure.adapter.out.persistence.repository;

import com.jpau.challenge.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    // the query only determines which prices are valid. Priority is evaluated by the use case.
    @Query("""
            SELECT price
            FROM PriceEntity price
            WHERE price.productId = :productId
              AND price.brandId = :brandId
              AND price.startDate <= :date
              AND price.endDate >= :date
            """)
    List<PriceEntity> findPrices(
            @Param("date") LocalDateTime date,
            @Param("productId") Long productId,
            @Param("brandId") Long brandId
    );

}