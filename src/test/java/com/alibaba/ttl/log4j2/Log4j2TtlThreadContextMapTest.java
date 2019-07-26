package com.alibaba.ttl.log4j2;

import com.alibaba.ttl.TtlRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class Log4j2TtlThreadContextMapTest extends AbstractTest4StdOut {

    @Test
    public void test_log4j2_ThreadContext() throws Exception {
        final Logger logger = LogManager.getLogger(Log4j2TtlThreadContextMapTest.class);

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
        ThreadContext.put(TRACE_ID, TRACE_ID_VALUE);

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
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                // Log in thread pool
                ThreadContext.put("task", "" + START_TIME_STAMP);
                logger.info("Log in Runnable!");
            }
        };
        return TtlRunnable.get(task);
    }
}
