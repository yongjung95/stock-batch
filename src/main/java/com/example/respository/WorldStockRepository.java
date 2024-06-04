package com.example.respository;

import com.example.domain.WorldStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldStockRepository extends JpaRepository<WorldStock, Long> {

    WorldStock findBySymbol(String symbol);
}
