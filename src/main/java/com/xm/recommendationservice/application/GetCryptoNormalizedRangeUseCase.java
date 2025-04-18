package com.xm.recommendationservice.application;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.xm.recommendationservice.domain.CryptoPrice;
import com.xm.recommendationservice.domain.CryptoPriceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCryptoNormalizedRangeUseCase {
    private final CryptoPriceRepository repository;

    public List<CryptoNormalizedRange> getAllNormalizedRanges(LocalDate startDate, LocalDate endDate) {
        List<CryptoPrice> allPrices = List.of();
        if (startDate == null && endDate == null) {
            allPrices = repository.findAll();
        } else if (startDate != null && endDate != null) {
            allPrices = repository.findAllByTimestampBetween(
                    startDate.atStartOfDay().toInstant(ZoneOffset.UTC),
                    endDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC));
        } else if (startDate != null || endDate != null) {
            throw new InvalidParameterException("Both start and end dates must be provided");
        }

        Map<String, List<CryptoPrice>> grouped = allPrices.stream()
                .collect(Collectors.groupingBy(CryptoPrice::getSymbol));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String symbol = entry.getKey();
                    List<CryptoPrice> prices = entry.getValue();
                    CryptoPrice oldest = Collections.min(prices, Comparator.comparing(CryptoPrice::getTimestamp));
                    CryptoPrice newest = Collections.max(prices, Comparator.comparing(CryptoPrice::getTimestamp));
                    double min = prices.stream().mapToDouble(CryptoPrice::getPrice).min().orElse(0);
                    double max = prices.stream().mapToDouble(CryptoPrice::getPrice).max().orElse(0);
                    double normalized = min > 0 ? (max - min) / min : 0;
                    return new CryptoNormalizedRange(symbol, normalized, oldest.getTimestamp(), newest.getTimestamp());
                })
                .sorted(Comparator.comparingDouble(CryptoNormalizedRange::getNormalizedRange).reversed())
                .toList();
    }

    public CryptoNormalizedRange getTopCryptoByNormalizedRangeForDay(LocalDate date) {
        if (date == null) {
            throw new InvalidParameterException("Date must be provided");
        }

        Instant start = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<CryptoPrice> prices = repository.findAllByTimestampBetween(start, end);

        Map<String, List<CryptoPrice>> grouped = prices.stream()
                .collect(Collectors.groupingBy(CryptoPrice::getSymbol));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String symbol = entry.getKey();
                    List<CryptoPrice> symbolPrices = entry.getValue();
                    double min = symbolPrices.stream().mapToDouble(CryptoPrice::getPrice).min().orElse(0);
                    double max = symbolPrices.stream().mapToDouble(CryptoPrice::getPrice).max().orElse(0);
                    double normalized = min > 0 ? (max - min) / min : 0;
                    return new CryptoNormalizedRange(symbol, normalized, start, end);
                })
                .max(Comparator.comparingDouble(CryptoNormalizedRange::getNormalizedRange))
                .orElseThrow(() -> new NoSuchElementException("No data for date: " + date));
    }
}
