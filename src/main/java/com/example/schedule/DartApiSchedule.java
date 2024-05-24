package com.example.schedule;

import com.example.service.DartApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class DartApiSchedule {

    private boolean isInitialRun = true;

    private final DartApiService service;

    @Autowired
    public DartApiSchedule(DartApiService service) {
        // 빈 초기화 시 첫 실행 설정
        this.service = service;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 서버 시작 시 한 번 실행
        reportCurrentTime();
        isInitialRun = false;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void reportCurrentTime() {
        if (isInitialRun) {
            // 서버 시작 시 한 번 실행한 이후 주기적으로 실행
            service.stockInfoUpdate();
        } else {
            service.stockInfoUpdate();
        }
    }
}
