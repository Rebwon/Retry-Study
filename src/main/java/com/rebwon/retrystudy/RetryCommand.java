package com.rebwon.retrystudy;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryCommand<T> {

    private int retryCounter;
    private final int maxRetries;

    public RetryCommand(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public T run(Supplier<T> function) {
        try {
            return function.get();
        } catch (Exception e) {
            return retry(function);
        }
    }

    public int getRetryCounter() {
        return retryCounter;
    }

    private T retry(Supplier<T> function) throws RuntimeException {
        log.info("FAILED - Command failed, will be retried " + maxRetries + "times.");
        retryCounter = 0;
        while (retryCounter < maxRetries) {
            try {
                return function.get();
            }
            catch (Exception e) {
                retryCounter++;
                log.info("FAILED - Command failed on retry " + retryCounter + " of " + maxRetries + " error: " + e);
                if (retryCounter >= maxRetries) {
                    log.info("Max retries exceeded.");
                    break;
                }

            }
        }
        throw new RuntimeException("Command failed on all of " + maxRetries + " retries");
    }
}
