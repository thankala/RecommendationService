package com.xm.recommendationservice.domain;

import java.time.Instant;
import java.util.List;

public interface CryptoPriceRepository {
    List<CryptoPrice> findAll();

    List<CryptoPrice> findAllBySymbol(String symbol);

    List<CryptoPrice> findAllByTimestampBetween(Instant start, Instant end);

    List<CryptoPrice> findAllBySymbolAndTimestampBetween(String symbol, Instant start, Instant end);

    List<CryptoPrice> saveAll(List<CryptoPrice> prices);
}
