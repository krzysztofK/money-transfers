package org.krzysztofk.transfers.accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

class Retrier {

    private static Logger LOG = LoggerFactory.getLogger(Retrier.class);

    static <T> T executeWithRetry(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (ConcurrentUpdateException exception) {
                LOG.warn("Concurrent update failed. will be retried");
            }
        }
    }

    static class ConcurrentUpdateException extends RuntimeException {
    }
}
