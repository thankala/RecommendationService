package com.xm.recommendationservice.application;

import java.time.LocalDate;
import java.util.List;

import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;

public interface GetCryptoNormalizedRangeUseCase {
    List<CryptoNormalizedRange> getAllNormalizedRanges(LocalDate startDate, LocalDate endDate);

    CryptoNormalizedRange getTopCryptoByNormalizedRangeForDay(LocalDate date);
}
