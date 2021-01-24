package com.alibaba.ttl.log4j2;

import com.alibaba.ttl.TtlRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Slf4jMdcDemo {
    private static final Logger logger = LoggerFactory.getLogger(Slf4jMdcDemo.class);

    public static void main(String[] args) throws Exception {
        // Log in Main Thread
        logger.info("Log in main!");

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Run task in thread pool
        executorService.submit(createTask()).get();

        // Init Log Context, set TTL
        // More KV if needed
        final String TRACE_ID = "trace-id";
        final String TRACE_ID_VALUE = "XXX-YYY-ZZZ";
        MDC.put(TRACE_ID, TRACE_ID_VALUE);

        // Log in Main Thread
        logger.info("Log in main!");

        executorService.submit(createTask()).get();

        logger.info("Exit main");

        executorService.shutdown();
    }

    private static Runnable createTask() {
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                // Log in thread pool
                MDC.put("task", new Date().toString());
                logger.info("Log in Runnable!");
            }
        };
        return TtlRunnable.get(task);
    }
}
