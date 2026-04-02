package com.xm.recommendationservice.infrastructure.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.xm.recommendationservice.domain.CryptoPriceRepository;
import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;
import com.xm.recommendationservice.domain.dtos.CryptoPrice;
import com.xm.recommendationservice.domain.dtos.CryptoStats;
import com.xm.recommendationservice.infrastructure.repository.entities.CryptoPriceEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CryptoPriceRepositoryImpl implements CryptoPriceRepository {
    private final CryptoPriceEntityRepository repository;

    private final JdbcTemplate jdbcTemplate;

    private final EntityMapper mapper;

    @Value("${crypto.db.batch-size:1000}")
    private int batchSize;

    private final String sql = "INSERT INTO crypto_prices (timestamp, symbol, price) VALUES (?, ?, ?) ON CONFLICT (timestamp, symbol) DO NOTHING";

    @Override
    public void batchInsertIgnoreDuplicates(List<CryptoPrice> prices) {
        var entityPrices = prices
                .stream()
                .map(mapper::to)
                .collect(Collectors.toList());

        batchInsert(entityPrices);
    }

    @Override
    public Optional<CryptoStats> getStatsBySymbolAndTimestampBetween(String symbol, Instant start, Instant end) {
        return repository.getCryptoStats(symbol, start, end)
                .stream()
                .map(mapper::to)
                .findFirst();

    }

    @Override
    public List<CryptoNormalizedRange> getAllNormalizedRangesBetween(Instant start, Instant end) {
        return repository.getAllNormalizedRangesBetween(start, end).stream()
                .map(mapper::to)
                .toList();
    }

    @Override
    public Optional<CryptoNormalizedRange> getTopNormalizedRangeBetween(Instant start, Instant end) {
        return repository.getAllNormalizedRangesBetween(start, end)
                .stream()
                .map(mapper::to)
                .findFirst();
    }

    private void batchInsert(List<CryptoPriceEntity> prices) {
        jdbcTemplate.batchUpdate(
                sql,
                prices,
                batchSize,
                (PreparedStatement ps, CryptoPriceEntity price) -> {
                    ps.setTimestamp(1, Timestamp.from(price.getTimestamp()));
                    ps.setString(2, price.getSymbol());
                    ps.setDouble(3, price.getPrice());
                });
    }
}
