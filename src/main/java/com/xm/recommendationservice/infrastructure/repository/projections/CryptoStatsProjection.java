package com.xm.recommendationservice.infrastructure.repository.projections;

import java.time.Instant;

public interface CryptoStatsProjection {
    String getSymbol();

    Double getMin();

    Double getMax();

    Double getOldest();

    Double getNewest();

    Instant getOldestTimestamp();

    Instant getNewestTimestamp();
}
