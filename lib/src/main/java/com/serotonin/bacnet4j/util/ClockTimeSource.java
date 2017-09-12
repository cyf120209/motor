package com.serotonin.bacnet4j.util;

public class ClockTimeSource implements TimeSource {
    @Override
    public long currentTimeMillis() {
        return System.nanoTime()/1000000; //System.currentTimeMillis();
    }
}
