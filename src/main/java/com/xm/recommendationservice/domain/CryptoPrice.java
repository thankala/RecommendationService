package com.xm.recommendationservice.domain;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPrice {
    private Instant timestamp;
    private String symbol;
    private Double price;
}
