package com.example.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorldStockServiceTest {

    @Autowired
    WorldStockService worldStockService;

    @Test
    void 해외주식_종목호출테스트() {
        worldStockService.getStockInfo("AMEX");
    }
}