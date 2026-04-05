package com.xm.recommendationservice.application;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.xm.recommendationservice.domain.CryptoPriceRepository;
import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCryptoNormalizedRangeUseCaseImpl implements GetCryptoNormalizedRangeUseCase {
    private final CryptoPriceRepository repository;

    public List<CryptoNormalizedRange> getAllNormalizedRanges(LocalDate startDate, LocalDate endDate) {
        Instant start = Optional.ofNullable(startDate).map(s -> s.atStartOfDay().toInstant(ZoneOffset.UTC))
                .orElse(null);
        Instant end = Optional.ofNullable(endDate).map(e -> e.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))
                .orElse(null);

        return repository.getAllNormalizedRangesBetween(start, end);
    }

    public CryptoNormalizedRange getTopCryptoByNormalizedRangeForDay(LocalDate date) {
        if (date == null) {
            throw new InvalidParameterException("Date must be provided");
        }

        Instant start = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        return repository.getTopNormalizedRangeBetween(start, end)
                .orElseThrow(() -> new NoSuchElementException("No data for date: " + date));
    }
}
