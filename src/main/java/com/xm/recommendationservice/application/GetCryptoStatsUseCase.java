package com.xm.recommendationservice.application;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.xm.recommendationservice.domain.CryptoPrice;
import com.xm.recommendationservice.domain.CryptoPriceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCryptoStatsUseCase {
    private final CryptoPriceRepository repository;

    public CryptoStats getStatsForCrypto(String symbol) {
        // Get oldest/newest/min/max prices for a symbol
        List<CryptoPrice> prices = repository.findAllBySymbol(symbol);

        if (prices.isEmpty()) {
            throw new NoSuchElementException("Crypto not found: " + symbol);
        }

        CryptoPrice oldest = Collections.min(prices, Comparator.comparing(CryptoPrice::getTimestamp));
        CryptoPrice newest = Collections.max(prices, Comparator.comparing(CryptoPrice::getTimestamp));
        double min = prices.stream().mapToDouble(CryptoPrice::getPrice).min().orElse(0);
        double max = prices.stream().mapToDouble(CryptoPrice::getPrice).max().orElse(0);

        return new CryptoStats(symbol, oldest.getPrice(), newest.getPrice(), min, max, oldest.getTimestamp(),
                newest.getTimestamp());
    }

}
