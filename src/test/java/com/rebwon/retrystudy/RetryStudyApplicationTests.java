package com.rebwon.retrystudy;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = RetryConfig.class,
    loader = AnnotationConfigContextLoader.class
)
class RetryStudyApplicationTests {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private RetryTemplate retryTemplate;

    @Test
    void sut_retry_service() {
        assertThatExceptionOfType(RuntimeException.class)
            .isThrownBy(() -> retryTemplate.execute((RetryCallback<Void, RuntimeException>) context -> {
                sampleService.doSomething();
                return null;
            }));
    }

}
