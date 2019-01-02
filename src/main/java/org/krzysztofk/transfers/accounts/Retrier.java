package org.krzysztofk.transfers.accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

class Retrier {

    private static Logger LOG = LoggerFactory.getLogger(Retrier.class);
    private static final int MAX_RETRIES = 100;

    static <T> T executeWithRetry(Supplier<T> supplier) {
        for (int retriesCount = 0; retriesCount < MAX_RETRIES; retriesCount++) {
            try {
                return supplier.get();
            } catch (ConcurrentUpdateException exception) {
                LOG.warn("Concurrent update failed");
            }
        }
        throw new RuntimeException("Operation failed after " + MAX_RETRIES + " retries");
    }

    static class ConcurrentUpdateException extends RuntimeException {
    }
}
