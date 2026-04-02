package com.xm.recommendationservice.api.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xm.recommendationservice.application.GetCryptoNormalizedRangeUseCase;
import com.xm.recommendationservice.application.GetCryptoStatsUseCase;
import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;
import com.xm.recommendationservice.domain.dtos.CryptoStats;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cryptos")
@RequiredArgsConstructor
public class CryptoController implements CryptoApi {

        private final GetCryptoStatsUseCase getCryptoStatsUseCase;
        private final GetCryptoNormalizedRangeUseCase getCryptoNormalizedRangeUseCase;

        @Override
        @GetMapping("/{symbol}/stats")
        public CryptoStats getStatsForCrypto(
                        @PathVariable String symbol,
                        @RequestParam(value = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @RequestParam(value = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
                return getCryptoStatsUseCase.getStatsForCrypto(symbol, startDate, endDate);
        }

        @Override
        @GetMapping("/top-normalized-range")
        public CryptoNormalizedRange getTopCryptoForDay(
                        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
                return getCryptoNormalizedRangeUseCase.getTopCryptoByNormalizedRangeForDay(date);
        }

        @Override
        @GetMapping("/normalized-range")
        public List<CryptoNormalizedRange> getAllSortedByNormalizedRange(
                        @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
                return getCryptoNormalizedRangeUseCase.getAllNormalizedRanges(startDate, endDate);
        }
}
