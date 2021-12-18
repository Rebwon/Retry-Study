package com.rebwon.retrystudy;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

@RequiredArgsConstructor
public class SampleService {

    @Retryable(value = RuntimeException.class,
        maxAttemptsExpression = "${retry.maxAttempts}",
        backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public void retryMethod() {

    }

    @Retryable(value = SQLException.class,
        maxAttempts = 2, backoff = @Backoff(delay = 100))
    public void retryMethod(String sql) throws SQLException {

    }

    @Recover
    public void recoverMethod(SQLException e, String sql) {

    }

    public void doSomething() {
        throw new RuntimeException();
    }
}
