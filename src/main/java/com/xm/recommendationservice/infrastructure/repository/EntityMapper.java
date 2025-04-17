package com.xm.recommendationservice.infrastructure.repository;

import org.springframework.stereotype.Component;

import com.xm.recommendationservice.domain.CryptoPrice;
import com.xm.recommendationservice.infrastructure.repository.entities.CryptoPriceEntity;

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
}
