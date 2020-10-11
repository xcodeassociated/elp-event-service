package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.service.command.AsycServiceCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/event/api/v1/async")
public class AsyncControllerV1 {
    private final AsycServiceCommand asycServiceCommand;

    @GetMapping("/execute")
    public ResponseEntity<String> execute() {
        log.info("Processing `start` request in AsyncControllerV1");
        CompletableFuture<String> asyncResult =  asycServiceCommand.doAsyncProcess();
        log.info("Waiting for computable future to be completed...");

        // do something else here...

        String data = asyncResult.join();
        log.info("Task completed with result: {}", data);
        return ResponseEntity.ok().body(data);
    }
}
