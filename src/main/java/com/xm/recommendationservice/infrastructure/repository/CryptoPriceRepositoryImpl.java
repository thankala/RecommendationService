package com.xm.recommendationservice.infrastructure.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.xm.recommendationservice.domain.CryptoPriceRepository;
import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;
import com.xm.recommendationservice.domain.dtos.CryptoStats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CryptoPriceRepositoryImpl implements CryptoPriceRepository {
    private final CryptoPriceEntityRepository repository;

    private final EntityMapper mapper;

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
}
