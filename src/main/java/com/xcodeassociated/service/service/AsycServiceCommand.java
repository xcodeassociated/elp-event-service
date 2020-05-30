package com.xcodeassociated.service.service;

import java.util.concurrent.CompletableFuture;

public interface AsycServiceCommand {
    CompletableFuture<String> doAsyncProcess();
}
