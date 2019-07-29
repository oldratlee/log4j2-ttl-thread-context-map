package com.alibaba.ttl.log4j2.bug;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.MDC;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

import static com.alibaba.ttl.TransmittableThreadLocal.Transmitter.*;

@Slf4j
public class Log4j2Test {
    static {
        // WIP! Not works yet!!

        // TTL integration for Reactor
        Schedulers.addExecutorServiceDecorator("TransmittableThreadLocal",
            (scheduler, scheduledExecutorService) -> TtlExecutors.getTtlScheduledExecutorService(scheduledExecutorService));

        Hooks.onEachOperator("TransmittableThreadLocal", objectPublisher -> new Publisher<Object>() {
            final Object capture = capture();
            @Override
            public void subscribe(Subscriber<? super Object> s) {
                final Object backup = replay(capture);
                try {
                    objectPublisher.subscribe(s);
                } finally {
                    restore(backup);
                }
            }
        });
    }

    private static final String REQUEST_ID = "request-id";

    @Test
    public void testLogWithWebClient() throws Exception {
        final TransmittableThreadLocal<String> ttl = new TransmittableThreadLocal<String>();

        final WebClient webClient = WebClient.create();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        MDC.put(REQUEST_ID, "1");
        ttl.set("11");
        log.info("start first call");
        webClient
            .get()
            .uri("http://www.baidu.com/error")
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> {
                log.error("[{}] exception: {}", ttl.get(), e.getLocalizedMessage());
                return Mono.just("hello");
            })
            .subscribe(x -> {
                log.warn("[{}] received: {}", ttl.get(), x.replace("\n", "|"));
                countDownLatch.countDown();
            });

        countDownLatch.await();


        CountDownLatch countDownLatch2 = new CountDownLatch(1);

        MDC.put(REQUEST_ID, "2");
        ttl.set("22");
        log.info("start second call");
        webClient
            .get()
            .uri("http://www.baidu.com/error")
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> {
                log.error("[{}] exception: {}", ttl.get(), e.getLocalizedMessage());
                return Mono.just("hello");
            })
            .subscribe(x -> {
                log.warn("[{}] received: {}", ttl.get(), x.replace("\n", "|"));
                countDownLatch2.countDown();
            });

        countDownLatch2.await();
    }
}
