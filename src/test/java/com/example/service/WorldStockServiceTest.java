package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class WorldStockServiceTest {

    @Autowired
    WorldStockService worldStockService;

    @Test
    void 해외주식_종목호출테스트() {
        worldStockService.worldStockInfoUpdate("AMEX");
    }


    @Test
    void NOTIFY_토큰정보_가져오기() {
        String token = worldStockService.getToken();

        log.info("token = " + token);
    }

    @Test
    void 해외주식_한글상품명_가져오기() {
        String koreanName = worldStockService.getStockKoreanName("AAPL", "NASDAQ");

        Assertions.assertThat(koreanName).isEqualTo("애플");
        log.info("koreanName = " + koreanName);

    }
}