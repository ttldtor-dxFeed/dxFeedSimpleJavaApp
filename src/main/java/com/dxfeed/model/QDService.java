package com.dxfeed.model;

import com.devexperts.logging.Logging;
import com.dxfeed.api.DXEndpoint;
import com.dxfeed.api.DXFeedTimeSeriesSubscription;
import com.dxfeed.event.market.TimeAndSale;
import com.dxfeed.tools.Speedometer;

import java.util.List;

public class QDService {
    private final Logging logger = Logging.getLogging(QDService.class);
    private final Speedometer speedometer;

    public QDService(Speedometer speedometer) {
        this.speedometer = speedometer;
    }

    public void testTnsSubscription(String address, List<String> symbols, long timeout) throws InterruptedException {
        logger.info("QDService: TnsSub: Connecting");

        try (DXEndpoint endpoint = DXEndpoint.newBuilder()
                .build()
                .connect(address)) {
            try (DXFeedTimeSeriesSubscription<TimeAndSale> sub = endpoint.getFeed().createTimeSeriesSubscription(TimeAndSale.class)) {
                sub.setFromTime(0L);
                sub.addEventListener(items -> {
                    speedometer.addEvents(items.size());
                });
                sub.addSymbols(symbols);
                long calculatedTimeout = timeout == 0L ? 1000000L : timeout;
                Thread.sleep(calculatedTimeout * 1000);
                logger.info("QDService: TnsSub: Disconnecting");
            }
        }
    }
}
