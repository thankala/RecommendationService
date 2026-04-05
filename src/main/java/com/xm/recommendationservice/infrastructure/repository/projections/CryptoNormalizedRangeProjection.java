package com.xm.recommendationservice.infrastructure.repository.projections;

import java.time.Instant;

public interface CryptoNormalizedRangeProjection {
    String getSymbol();

    Double getNormalizedRange();

    Instant getOldestTimestamp();

    Instant getNewestTimestamp();
}
