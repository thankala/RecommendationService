package com.xm.recommendationservice.infrastructure.repository.projections;

public interface CryptoNormalizedRangeProjection {
    String getSymbol();
    Double getNormalizedRange();
}
