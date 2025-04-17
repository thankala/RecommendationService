package com.xm.recommendationservice.application;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoStats {
    private String symbol;
    private double oldest;
    private double newest;
    private double min;
    private double max;
    private Instant oldestTimestamp;
    private Instant newestTimestamp;
}
