package com.xm.recommendationservice.application.scheduling;

import com.xm.recommendationservice.domain.CryptoPrice;
import com.xm.recommendationservice.domain.CryptoPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.file.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoadCryptoPricesJob {

    private final CryptoPriceRepository repository;

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Value("${crypto.loader.folder}")
    private String pricesFolderPath;

    @Scheduled(initialDelay = 1000, fixedRate = 60 * 60 * 1000) // Runs once after 1s and then every hour
    public void loadAllCsvFilesAsync() {
        try {
            Path folderPath = Paths.get(pricesFolderPath);
            log.info("Scanning folder: {}", folderPath);

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*_values.csv")) {
                for (Path path : stream) {
                    futures.add(CompletableFuture.runAsync(() -> processFile(path), executor));
                }
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            log.info("All CSV files processed.");

        } catch (Exception e) {
            log.error("Failed to scan or process CSV folder", e);
        }
    }

    private void processFile(Path path) {
        log.info("Processing file: {}", path.getFileName());

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            List<CryptoPrice> entities = reader.lines()
                    .skip(1) // skip header
                    .map(this::parseLine)
                    .collect(Collectors.toList());

            repository.saveAll(entities);
            log.info("Inserted {} records", path.getFileName());

        } catch (Exception e) {
            log.error("Error processing file " + path.getFileName(), e);
        }
    }

    private CryptoPrice parseLine(String line) {
        String[] parts = line.split(",");
        return CryptoPrice.builder()
                .timestamp(Instant.ofEpochMilli(Long.parseLong(parts[0])))
                .symbol(parts[1])
                .price(Double.parseDouble(parts[2]))
                .build();
    }
}
