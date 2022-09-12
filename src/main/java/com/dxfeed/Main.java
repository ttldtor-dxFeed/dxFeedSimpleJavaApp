package com.dxfeed;

import com.dxfeed.api.DXEndpoint;
import com.dxfeed.api.DXFeedSubscription;
import com.dxfeed.event.market.Quote;
import com.dxfeed.tools.Speedometer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.LongStream;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("scheme", "com.dxfeed.api.impl.DXFeedScheme");

        DXEndpoint endpoint = DXEndpoint.create();

        endpoint.connect("demo.dxfeed.com:7300");

        Speedometer speedometer = new Speedometer(5000);
        List<DXFeedSubscription<Quote>> subs = new ArrayList<>();

        LongStream.range(0, 10000).forEach(i -> {
            DXFeedSubscription<Quote> sub = endpoint.getFeed().createSubscription(Quote.class);
            sub.setExecutor(new ScheduledThreadPoolExecutor(1));
            sub.addEventListener(list -> speedometer.addEvents(list.size()));
            sub.addSymbols("AAPL");
            subs.add(sub);
        });

        Thread.sleep(10001000);
        subs.forEach(DXFeedSubscription::close);
        endpoint.close();
    }
}