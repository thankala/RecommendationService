package com.xm.recommendationservice.domain.dtos;

import lombok.*;

import java.time.Instant;

@Data
@Builder
public class CryptoPrice {
    private Instant timestamp;
    private String symbol;
    private Double price;
}
