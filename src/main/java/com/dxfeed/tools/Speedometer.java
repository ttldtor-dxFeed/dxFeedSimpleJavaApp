package com.dxfeed.tools;

import com.devexperts.logging.Logging;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class Speedometer {
    private final Logging logger = Logging.getLogging(Speedometer.class);
    private final AtomicLong counter = new AtomicLong(0L);
    private final AtomicLong lastCount = new AtomicLong(0L);
    private final long period;

    public Speedometer(long period) {
        this.period = period < 100 ? 1000 : period;

        Timer countingTimer = new Timer("CountingTimer");
        countingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                double eventsPerSecond = ((double) (counter.get() - lastCount.get())) / ((double) Speedometer.this.period) * 1000.0;

                logger.info(String.format("%.3f", eventsPerSecond) + " events/s");
                lastCount.set(counter.get());
            }
        }, 0, period);
    }

    public void addEvents(long count) {
        counter.addAndGet(count);
    }
}
