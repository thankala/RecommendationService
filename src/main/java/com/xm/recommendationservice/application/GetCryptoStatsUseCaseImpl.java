package com.xm.recommendationservice.application;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.xm.recommendationservice.domain.CryptoPriceRepository;
import com.xm.recommendationservice.domain.dtos.CryptoStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCryptoStatsUseCaseImpl implements GetCryptoStatsUseCase {
    private final CryptoPriceRepository repository;

    public CryptoStats getStatsForCrypto(String symbol, LocalDate startDate, LocalDate endDate) {
        Instant start = null;
        Instant end = null;

        if (startDate != null && endDate != null) {
            start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            end = endDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        } else if (startDate != null || endDate != null) {
            throw new InvalidParameterException("Both start and end dates must be provided");
        }

        return repository.getStatsBySymbolAndTimestampBetween(symbol, start, end)
                .orElseThrow(() -> new NoSuchElementException("Crypto not found: " + symbol));
    }

}
