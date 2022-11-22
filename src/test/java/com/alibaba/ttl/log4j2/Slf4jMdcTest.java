package com.alibaba.ttl.log4j2;

import com.alibaba.ttl.TtlRunnable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class Slf4jMdcTest extends AbstractTest4StdOut {
    @Test
    public void test_slf4j_MDC() throws Exception {
        final Logger logger = LoggerFactory.getLogger(Slf4jMdcTest.class);

        // Log in Main Thread
        logger.info("Log in main!");
        assertEquals("[] {} - Log in main!", getLogAndClear());

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Run task in thread pool
        executorService.submit(createTask(logger)).get();
        assertEquals("[] {task=" + START_TIME_STAMP + "} - Log in Runnable!", getLogAndClear());

        // Init Log Context, set TTL
        // More KV if needed
        final String TRACE_ID = "trace-id";
        final String TRACE_ID_VALUE = "XXX-YYY-ZZZ";
        MDC.put(TRACE_ID, TRACE_ID_VALUE);

        // Log in Main Thread
        logger.info("Log in main!");
        assertEquals("[XXX-YYY-ZZZ] {trace-id=XXX-YYY-ZZZ} - Log in main!", getLogAndClear());

        executorService.submit(createTask(logger)).get();
        assertEquals("[XXX-YYY-ZZZ] {task=" + START_TIME_STAMP + ", trace-id=XXX-YYY-ZZZ} - Log in Runnable!", getLogAndClear());

        logger.info("Exit main");
        assertEquals("[XXX-YYY-ZZZ] {trace-id=XXX-YYY-ZZZ} - Exit main", getLogAndClear());

        executorService.shutdown();
    }

    private static Runnable createTask(final Logger logger) {
        final Runnable task = () -> {
            // Log in thread pool
            MDC.put("task", "" + START_TIME_STAMP);
            logger.info("Log in Runnable!");
        };
        return TtlRunnable.get(task);
    }
}
