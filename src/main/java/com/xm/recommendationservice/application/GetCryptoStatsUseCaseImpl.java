package com.xm.recommendationservice.application;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.xm.recommendationservice.domain.CryptoPriceRepository;
import com.xm.recommendationservice.domain.dtos.CryptoStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCryptoStatsUseCaseImpl implements GetCryptoStatsUseCase {
    private final CryptoPriceRepository repository;

    public CryptoStats getStatsForCrypto(String symbol, LocalDate startDate, LocalDate endDate) {
        Instant start = Optional.ofNullable(startDate).map(s -> s.atStartOfDay().toInstant(ZoneOffset.UTC))
                .orElse(null);
        Instant end = Optional.ofNullable(endDate).map(e -> e.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))
                .orElse(null);

        return repository.getStatsBySymbolAndTimestampBetween(symbol, start, end)
                .orElseThrow(() -> new NoSuchElementException("Crypto not found: " + symbol));
    }

}
