package com.xm.recommendationservice.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xm.recommendationservice.infrastructure.repository.entities.CryptoPriceEntity;

import java.time.Instant;
import java.util.List;

public interface CryptoPriceEntityRepository extends JpaRepository<CryptoPriceEntity, Long> {

    List<CryptoPriceEntity> findAll();

    List<CryptoPriceEntity> findAllBySymbol(String symbol);

    List<CryptoPriceEntity> findAllByTimestampBetween(Instant start, Instant end);

    List<CryptoPriceEntity> findAllBySymbolAndTimestampBetween(String symbol, Instant start, Instant end);

    @Modifying
    @Query(value = "INSERT INTO crypto_prices (timestamp, symbol, price) " +
            "VALUES (:timestamp, :symbol, :price) " +
            "ON CONFLICT (symbol, timestamp) DO NOTHING", nativeQuery = true)
    void insertIfNotExists(@Param("timestamp") Instant timestamp,
            @Param("symbol") String symbol,
            @Param("price") Double price);
}