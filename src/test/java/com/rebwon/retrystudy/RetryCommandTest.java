package com.rebwon.retrystudy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class RetryCommandTest {

    public static final String SUCCESS = "success";
    public static final int MAX_RETRIES = 3;

    @Test
    void run_shouldNotRetryCommand_when_SuccessFul() {
        // Arrange
        var retryCommand = new RetryCommand<String>(MAX_RETRIES);
        Supplier<String> commandSucceed = () -> SUCCESS;

        // Act
        var result = retryCommand.run(commandSucceed);

        // Assert
        assertEquals(SUCCESS, result);
        assertEquals(0, retryCommand.getRetryCounter());
    }

    @Test
    void run_shouldRetryOnceThenSucceed_whenFailsOnFirstCallButSucceedsOnFirstRetry() {
        // Arrange
        var retryCommand = new RetryCommand<String>(MAX_RETRIES);
        Supplier<String> commandFailOnce = () -> {
            if (retryCommand.getRetryCounter() == 0) throw new RuntimeException("Command Failed");
            else return SUCCESS;
        };

        // Act
        var result = retryCommand.run(commandFailOnce);

        // Assert
        assertEquals(SUCCESS, result);
        assertEquals(1, retryCommand.getRetryCounter());
    }

    @Test
    void run_shouldThrowException_whenMaxRetriesIsReached() {
        // Arrange
        var retryCommand = new RetryCommand<String>(MAX_RETRIES);
        Supplier<String> commandFail = () -> {
            throw new RuntimeException("Failed");
        };

        // Act & Assert
        try {
            retryCommand.run(commandFail);
            fail("Should throw exception when max retries is reached");
        } catch (Exception ignored) { }
    }

    // UseCase
    public static final class MyGateway {
        private RestClient restClient;
        private RetryCommand<String> retryCommand;

        public MyGateway(RestClient restClient, int maxRetries) {
            this.restClient = restClient;
            this.retryCommand = new RetryCommand<>(maxRetries);
        }

        public String getThing(String id) {
            return retryCommand.run(() -> restClient.getThatThing(id));
        }
    }

    public static class RestClient {

        public String getThatThing(String id) {
            return id;
        }
    }
}