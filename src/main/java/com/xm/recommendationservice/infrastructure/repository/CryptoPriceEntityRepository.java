package com.xm.recommendationservice.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xm.recommendationservice.infrastructure.repository.entities.CryptoPriceEntity;

import java.util.List;

public interface CryptoPriceEntityRepository extends JpaRepository<CryptoPriceEntity, Long> {

    List<CryptoPriceEntity> findAll();

    List<CryptoPriceEntity> findAllBySymbol(String symbol);
}