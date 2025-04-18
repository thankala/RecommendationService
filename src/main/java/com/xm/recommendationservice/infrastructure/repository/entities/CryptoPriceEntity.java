package com.xm.recommendationservice.infrastructure.repository.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "crypto_prices", uniqueConstraints = {
        @UniqueConstraint(name = "uc_crypto_symbol_timestamp", columnNames = { "symbol", "timestamp" })
}, indexes = {
        @Index(name = "idx_symbol", columnList = "symbol"),
        @Index(name = "idx_timestamp", columnList = "timestamp"),
        @Index(name = "idx_symbol_timestamp", columnList = "symbol, timestamp")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;

    private String symbol;

    private Double price;
}
