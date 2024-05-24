package com.example.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DartApiServiceTest {

    @Autowired
    DartApiService service;

    @Test
    void 다트API_테스트() {
        service.stockInfoUpdate();
    }

}