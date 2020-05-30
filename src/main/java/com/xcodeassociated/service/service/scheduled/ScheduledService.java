package com.xcodeassociated.service.service.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableAsync
@EnableScheduling
public class ScheduledService {

    @Scheduled(fixedRateString = "${service.schedule.interval:1000}")
    void scheduledTask() {
        log.info("Synchronous task started on thread id: {}", Thread.currentThread().getId());
        // ...
    }

    @Async
    @Scheduled(fixedRateString = "${service.schedule.interval:1000}")
    void asyncScheduledTask() {
        log.info("Async task started on thread id: {}", Thread.currentThread().getId());
        // ...
    }
}
