package com.xm.recommendationservice.domain.dtos;

import java.time.Instant;

import lombok.*;

@Data
@Builder
public class CryptoStats {
    private String symbol;
    private double oldest;
    private double newest;
    private double min;
    private double max;
    private Instant oldestTimestamp;
    private Instant newestTimestamp;
}
