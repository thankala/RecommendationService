package com.xm.recommendationservice.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xm.recommendationservice.application.CryptoStats;
import com.xm.recommendationservice.application.GetCryptoStatsUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cryptos")
@RequiredArgsConstructor
public class CryptoController {

    private final GetCryptoStatsUseCase getCryptoStatsUseCase;

    // Get oldest/newest/min/max for a specific crypto
    @GetMapping("/{symbol}/stats")
    public CryptoStats getStatsForCrypto(@PathVariable String symbol) {
        return getCryptoStatsUseCase.getStatsForCrypto(symbol);
    }

}
