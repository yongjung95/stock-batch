package com.example.respository;

import com.example.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock findByCorpNameAndStockCode(String corpName, String stockCode);
}
