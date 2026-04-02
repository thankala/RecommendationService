package com.xm.recommendationservice.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xm.recommendationservice.infrastructure.repository.entities.CryptoPriceEntity;
import com.xm.recommendationservice.infrastructure.repository.projections.CryptoNormalizedRangeProjection;
import com.xm.recommendationservice.infrastructure.repository.projections.CryptoStatsProjection;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CryptoPriceEntityRepository extends JpaRepository<CryptoPriceEntity, Long> {

        @Query(value = """
                        SELECT DISTINCT
                            symbol,
                            FIRST_VALUE(price) OVER (PARTITION BY symbol ORDER BY timestamp ASC) as oldest,
                            FIRST_VALUE(price) OVER (PARTITION BY symbol ORDER BY timestamp DESC) as newest,
                            MIN(price) OVER (PARTITION BY symbol) as min,
                            MAX(price) OVER (PARTITION BY symbol) as max,
                            MIN(timestamp) OVER (PARTITION BY symbol) as oldestTimestamp,
                            MAX(timestamp) OVER (PARTITION BY symbol) as newestTimestamp
                        FROM crypto_prices
                        WHERE symbol = :symbol
                        AND (CAST(:start AS TIMESTAMP) IS NULL OR timestamp >= :start)
                        AND (CAST(:end AS TIMESTAMP) IS NULL OR timestamp <= :end)
                        """, nativeQuery = true)
        Optional<CryptoStatsProjection> getCryptoStats(@Param("symbol") String symbol,
                        @Param("start") Instant start,
                        @Param("end") Instant end);

        @Query(value = """
                        SELECT
                            symbol,
                            (MAX(price) - MIN(price)) / NULLIF(MIN(price), 0) as normalizedRange
                        FROM crypto_prices
                        WHERE (CAST(:start AS TIMESTAMP) IS NULL OR timestamp >= :start)
                        AND (CAST(:end AS TIMESTAMP) IS NULL OR timestamp <= :end)
                        GROUP BY symbol
                        ORDER BY normalizedRange DESC
                        """, nativeQuery = true)
        List<CryptoNormalizedRangeProjection> getAllNormalizedRangesBetween(@Param("start") Instant start,
                        @Param("end") Instant end);
}