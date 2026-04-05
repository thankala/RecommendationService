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
            WITH aggregates AS (
            	SELECT
            		c.symbol as symbol,
                    MIN(c.price) as min,
                    MAX(c.price) as max,
                    MIN(c.timestamp) as oldestTimestamp,
                    MAX(c.timestamp) as newestTimestamp
                FROM crypto_prices as c
                WHERE (CAST(:start AS TIMESTAMP) IS NULL OR c.timestamp >= :start)
                  AND (CAST(:end AS TIMESTAMP) IS NULL OR c.timestamp <= :end)
                GROUP by c.symbol
            )
            select
                a.symbol,

                (SELECT c1.price FROM crypto_prices as c1
                 where c1.symbol = :symbol and c1.timestamp = a.oldestTimestamp
                 LIMIT 1) as oldest,

                (SELECT c2.price FROM crypto_prices as c2
                 WHERE c2.symbol = :symbol and c2.timestamp = a.newestTimestamp
                 LIMIT 1) as newest,

                a.min,
                a.max,
                a.oldestTimestamp,
                a.newestTimestamp
            FROM aggregates as a
            WHERE symbol = :symbol;
                        """, nativeQuery = true)
    Optional<CryptoStatsProjection> getCryptoStats(
            @Param("symbol") String symbol,
            @Param("start") Instant start,
            @Param("end") Instant end);

    @Query(value = """
            SELECT
                symbol,
                (MAX(price) - MIN(price)) / NULLIF(MIN(price), 0) as normalizedRange,
                MIN(timestamp) as oldestTimestamp,
                MAX(timestamp) as newestTimestamp
            FROM crypto_prices
            WHERE (CAST(:start AS TIMESTAMP) IS NULL OR timestamp >= :start)
            AND (CAST(:end AS TIMESTAMP) IS NULL OR timestamp <= :end)
            GROUP BY symbol
            ORDER BY normalizedRange DESC
            """, nativeQuery = true)
    List<CryptoNormalizedRangeProjection> getAllNormalizedRangesBetween(@Param("start") Instant start,
            @Param("end") Instant end);
}