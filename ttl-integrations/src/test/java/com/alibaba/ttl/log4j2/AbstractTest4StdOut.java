package com.alibaba.ttl.log4j2;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;

public abstract class AbstractTest4StdOut {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    protected static final long START_TIME_STAMP = System.currentTimeMillis();

    protected String getLogAndClear() {
        System.out.flush();

        final String out = systemOutRule.getLog();
        systemOutRule.clearLog();

        final int idx = out.indexOf('\n');
        return out.substring(idx + 1, out.length() - 1); // trim first line and last '\n'
    }
}
