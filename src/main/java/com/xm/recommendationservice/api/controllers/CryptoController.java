package com.xm.recommendationservice.api.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xm.recommendationservice.application.GetCryptoNormalizedRangeUseCase;
import com.xm.recommendationservice.application.GetCryptoStatsUseCase;
import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;
import com.xm.recommendationservice.domain.dtos.CryptoStats;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cryptos")
public class CryptoController implements CryptoApi {

        private final GetCryptoStatsUseCase getCryptoStatsUseCase;
        private final GetCryptoNormalizedRangeUseCase getCryptoNormalizedRangeUseCase;

        @Override
        @GetMapping("/{symbol}/stats")
        public ResponseEntity<CryptoStats> getStatsForCryptoBetween(
                        String symbol,
                        LocalDate startDate,
                        LocalDate endDate) {
                CryptoStats stats = getCryptoStatsUseCase.getStatsForCrypto(symbol, startDate, endDate);
                return ResponseEntity.ok(stats);
        }

        @Override
        @GetMapping("/top-normalized-range")
        public ResponseEntity<CryptoNormalizedRange> getTopCryptoForDay(LocalDate date) {
                CryptoNormalizedRange topCrypto = getCryptoNormalizedRangeUseCase
                                .getTopCryptoByNormalizedRangeForDay(date);
                return ResponseEntity.ok(topCrypto);
        }

        @Override
        @GetMapping("/normalized-range")
        public ResponseEntity<List<CryptoNormalizedRange>> getAllSortedByNormalizedRangeBetween(
                        LocalDate startDate,
                        LocalDate endDate) {
                List<CryptoNormalizedRange> ranges = getCryptoNormalizedRangeUseCase.getAllNormalizedRanges(startDate,
                                endDate);
                return ResponseEntity.ok(ranges);
        }
}
