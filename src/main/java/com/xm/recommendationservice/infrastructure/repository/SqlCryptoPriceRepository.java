package com.xm.recommendationservice.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xm.recommendationservice.domain.CryptoPrice;
import com.xm.recommendationservice.domain.CryptoPriceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SqlCryptoPriceRepository implements CryptoPriceRepository {
    private final CryptoPriceEntityRepository repository;

    private final EntityMapper mapper;

    @Override
    public List<CryptoPrice> findAll() {
        return repository.findAll().stream()
                .map(mapper::to)
                .toList();
    }

    @Override
    public List<CryptoPrice> findAllBySymbol(String symbol) {
        return repository.findAllBySymbol(symbol).stream()
                .map(mapper::to)
                .toList();
    }

    @Override
    public List<CryptoPrice> saveAll(List<CryptoPrice> prices) {
        return repository.saveAll(prices.stream()
                .map(mapper::to)
                .toList()).stream()
                .map(mapper::to)
                .toList();
    }

}
