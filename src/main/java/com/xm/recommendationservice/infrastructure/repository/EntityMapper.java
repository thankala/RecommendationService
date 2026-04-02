package com.xm.recommendationservice.infrastructure.repository;

import org.springframework.stereotype.Component;

import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;
import com.xm.recommendationservice.domain.dtos.CryptoPrice;
import com.xm.recommendationservice.domain.dtos.CryptoStats;
import com.xm.recommendationservice.infrastructure.repository.entities.CryptoPriceEntity;
import com.xm.recommendationservice.infrastructure.repository.projections.CryptoStatsProjection;
import com.xm.recommendationservice.infrastructure.repository.projections.CryptoNormalizedRangeProjection;

@Component
public class EntityMapper {

    public CryptoPriceEntity to(CryptoPrice price) {
        return CryptoPriceEntity.builder()
                .timestamp(price.getTimestamp())
                .symbol(price.getSymbol())
                .price(price.getPrice())
                .build();
    }

    public CryptoPrice to(CryptoPriceEntity entity) {
        return CryptoPrice.builder()
                .timestamp(entity.getTimestamp())
                .symbol(entity.getSymbol())
                .price(entity.getPrice())
                .build();
    }

    public CryptoStats to(CryptoStatsProjection projection) {
        return CryptoStats.builder()
                .symbol(projection.getSymbol())
                .min(projection.getMin())
                .max(projection.getMax())
                .oldest(projection.getOldest())
                .newest(projection.getNewest())
                .oldestTimestamp(projection.getOldestTimestamp())
                .newestTimestamp(projection.getNewestTimestamp())
                .build();
    }

    public CryptoNormalizedRange to(CryptoNormalizedRangeProjection projection) {
        return CryptoNormalizedRange.builder()
                .symbol(projection.getSymbol())
                .normalizedRange(projection.getNormalizedRange())
                .build();
    }
}
