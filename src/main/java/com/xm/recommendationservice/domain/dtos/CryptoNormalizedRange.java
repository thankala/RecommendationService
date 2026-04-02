package com.xm.recommendationservice.domain.dtos;

import java.time.Instant;

import lombok.*;


@Data
@Builder
public class CryptoNormalizedRange {
    private String symbol;
    private double normalizedRange;
    private Instant startDate;
    private Instant endDate;
}
