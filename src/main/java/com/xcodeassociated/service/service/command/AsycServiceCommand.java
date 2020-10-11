package com.xcodeassociated.service.service.command;

import java.util.concurrent.CompletableFuture;

public interface AsycServiceCommand {
    CompletableFuture<String> doAsyncProcess();
}
