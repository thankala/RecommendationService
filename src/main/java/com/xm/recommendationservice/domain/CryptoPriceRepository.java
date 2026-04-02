package com.xm.recommendationservice.domain;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;
import com.xm.recommendationservice.domain.dtos.CryptoStats;

public interface CryptoPriceRepository {

    Optional<CryptoStats> getStatsBySymbolAndTimestampBetween(String symbol, Instant start, Instant end);

    List<CryptoNormalizedRange> getAllNormalizedRangesBetween(Instant start, Instant end);

    Optional<CryptoNormalizedRange> getTopNormalizedRangeBetween(Instant start, Instant end);
}
