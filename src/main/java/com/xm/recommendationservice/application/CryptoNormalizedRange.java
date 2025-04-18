package com.xm.recommendationservice.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoNormalizedRange {
    private String symbol;
    private double normalizedRange;
}
