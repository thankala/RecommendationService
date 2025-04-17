package com.xm.recommendationservice.domain;

import java.util.List;

public interface CryptoPriceRepository {
    List<CryptoPrice> findAll();

    List<CryptoPrice> findAllBySymbol(String symbol);

    List<CryptoPrice> saveAll(List<CryptoPrice> prices);
}
