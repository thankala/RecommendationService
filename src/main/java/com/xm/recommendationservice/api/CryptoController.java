package com.xm.recommendationservice.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xm.recommendationservice.application.CryptoNormalizedRange;
import com.xm.recommendationservice.application.CryptoStats;
import com.xm.recommendationservice.application.GetCryptoNormalizedRangeUseCase;
import com.xm.recommendationservice.application.GetCryptoStatsUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cryptos")
@RequiredArgsConstructor
public class CryptoController {

    private final GetCryptoStatsUseCase getCryptoStatsUseCase;
    private final GetCryptoNormalizedRangeUseCase getCryptoNormalizedRangeUseCase;

    // Get oldest/newest/min/max for a specific crypto
    @GetMapping("/{symbol}/stats")
    public CryptoStats getStatsForCrypto(@PathVariable String symbol,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return getCryptoStatsUseCase.getStatsForCrypto(symbol, startDate, endDate);
    }

    // Get crypto with highest normalized range on a specific day
    @GetMapping("/top-normalized-range")
    public CryptoNormalizedRange getTopCryptoForDay(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return getCryptoNormalizedRangeUseCase.getTopCryptoByNormalizedRangeForDay(date);
    }

    @GetMapping("/normalized-range")
    public List<CryptoNormalizedRange> getAllSortedByNormalizedRange() {
        return getCryptoNormalizedRangeUseCase.getAllNormalizedRanges();
    }

}
