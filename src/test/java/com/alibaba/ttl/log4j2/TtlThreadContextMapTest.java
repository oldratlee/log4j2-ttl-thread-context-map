package com.alibaba.ttl.log4j2;

import com.alibaba.ttl.TtlRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class TtlThreadContextMapTest {
    private static final long START_TIME_STAMP = System.currentTimeMillis();

    private static final PrintStream stdOut = System.out;

    private static final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    // MUST hijack/set stdout before load logger instance(with stdout)!
    static {
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);
    }

    // then load logger instance
    private static Logger logger = LogManager.getLogger(TtlThreadContextMapTest.class);

    @AfterClass
    public static void afterClass() throws Exception {
        System.setOut(stdOut);
    }

    private static String getOutAndClear() throws Exception {
        System.out.flush();

        final byte[] bytes = byteArrayOutputStream.toByteArray();
        final String out = new String(bytes);
        byteArrayOutputStream.reset();

        stdOut.print(out);

        final int idx = out.indexOf('\n');
        return out.substring(idx + 1, out.length() - 1); // trim first line and last '\n'
    }

    @Test
    public void testName() throws Exception {
        // Log in Main Thread
        logger.info("Log in main!");
        assertEquals("[] {} - Log in main!",
                getOutAndClear());

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Run task in thread pool
        executorService.submit(createTask()).get();
        assertEquals("[] {task=" + START_TIME_STAMP + "} - Log in Runnable!",
                getOutAndClear());

        // Init Log Context, set TTL
        // More KV if needed
        final String TRACE_ID = "trace-id";
        final String TRACE_ID_VALUE = "XXX-YYY-ZZZ";
        ThreadContext.put(TRACE_ID, TRACE_ID_VALUE);

        // Log in Main Thread
        logger.info("Log in main!");
        assertEquals("[XXX-YYY-ZZZ] {trace-id=XXX-YYY-ZZZ} - Log in main!",
                getOutAndClear());

        executorService.submit(createTask()).get();
        assertEquals("[XXX-YYY-ZZZ] {task=" + START_TIME_STAMP + ", trace-id=XXX-YYY-ZZZ} - Log in Runnable!", getOutAndClear());

        logger.info("Exit main");
        assertEquals("[XXX-YYY-ZZZ] {trace-id=XXX-YYY-ZZZ} - Exit main",
                getOutAndClear());

        executorService.shutdown();
    }

    private static Runnable createTask() {
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
