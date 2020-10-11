package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.service.command.AsycServiceCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AsyncService implements AsycServiceCommand {

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> doAsyncProcess() {
        log.info("Starting `doAsyncProcess` in AsyncService");
        long startTime = System.currentTimeMillis();
        CompletableFuture<String> result = this.process();
        long estimatedTime = System.currentTimeMillis() - startTime;
        return result.thenApply((String data) -> data + ", in: " + estimatedTime + "ms");
    }

    private CompletableFuture<String> process() {
        int delay = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        log.info("Generated task delay: {}s", delay);
        try {
            // sample 10s task to do...
            TimeUnit.SECONDS.sleep(delay);

        } catch (InterruptedException e) {
            log.error("Handling `InterruptedException` in `doAsyncProcess` with message: {}", e.getMessage());
            e.printStackTrace();
        }
        // return some data from the task
        return CompletableFuture.supplyAsync(() -> "Task completed on thread: " + Thread.currentThread().getId());
    }
}
