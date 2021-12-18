package com.rebwon.retrystudy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

@EnableRetry
@Configuration
@PropertySource("classpath:retryConfig.properties")
public class RetryConfig {

    @Bean
    public SampleService sampleService() {
        return new SampleService();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplateBuilder()
            .fixedBackoff(2000l)
            .maxAttempts(2)
            .withListener(new DefaultListenerSupport())
            .build();
    }

    @Slf4j
    public static final class DefaultListenerSupport extends RetryListenerSupport {

        @Override
        public <T, E extends Throwable> void close(RetryContext context,
            RetryCallback<T, E> callback, Throwable throwable) {
            log.info("onClose");
            super.close(context, callback, throwable);
        }

        @Override
        public <T, E extends Throwable> void onError(RetryContext context,
            RetryCallback<T, E> callback, Throwable throwable) {
            log.info("onError");
            super.onError(context, callback, throwable);
        }

        @Override
        public <T, E extends Throwable> boolean open(RetryContext context,
            RetryCallback<T, E> callback) {
            log.info("onOpen");
            return super.open(context, callback);
        }
    }
}
