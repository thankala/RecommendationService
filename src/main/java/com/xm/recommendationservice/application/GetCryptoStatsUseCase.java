package com.xm.recommendationservice.application;

import java.time.LocalDate;

import com.xm.recommendationservice.domain.dtos.CryptoStats;

public interface GetCryptoStatsUseCase {
    CryptoStats getStatsForCrypto(String symbol, LocalDate startDate, LocalDate endDate);
}
