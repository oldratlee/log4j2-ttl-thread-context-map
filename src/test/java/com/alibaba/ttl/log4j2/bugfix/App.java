package com.alibaba.ttl.log4j2.bugfix;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Demo code for <a href="https://github.com/oldratlee/log4j2-ttl-thread-context-map/issues/8">issue #8</a>
 *
 * @author moonseen
 * @date 2020/10/14-12:40 AM
 */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final String DEBUG_KEY = "online-debug";

    public static void main(String[] args) {
        Service service = new Service();

        LOGGER.info("---------[Before Remove MDC]-------------");
        MDC.put(DEBUG_KEY, "true");
        service.execute(MDC.get(DEBUG_KEY)).join();

        MDC.remove(DEBUG_KEY);
        LOGGER.info("---------[After Remove MDC]-------------");
        service.execute(MDC.get(DEBUG_KEY)).join();
    }

    public static class Service {
        private static final Logger logger = LoggerFactory.getLogger(Service.class);
        private static final ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(4, r -> {
            final Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }));

        public CompletableFuture<?> execute(String mark) {
            final List<CompletableFuture<Void>> futures = new ArrayList<>();
            // change task number may get different result.
            final int TASK_COUNT = 5;
            for (int i = 0; i < TASK_COUNT; i++) {
                final int num = i;
                futures.add(CompletableFuture.runAsync(() -> {
                    if (logger.isDebugEnabled())
                        logger.debug("[parent: {}/child: {}] service running debug message: task {}", mark, MDC.get(DEBUG_KEY), num);
                    logger.info("[parent: {}/child: {}] service running debug message: task {}", mark, MDC.get(DEBUG_KEY), num);
                }, executorService));
            }

            return sequence(futures);
        }

        private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
            CompletableFuture<Void> future = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            return future.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
        }
    }
}
