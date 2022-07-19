package com.dxfeed;

import com.dxfeed.model.QDService;
import com.dxfeed.tools.Speedometer;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("scheme", "com.dxfeed.api.impl.DXFeedScheme");

        QDService qdService = new QDService(new Speedometer(5000));

        qdService.testTnsSubscription("192.168.0.149:8888", Arrays.asList("AAPL", "IBM", "ETH/USD:GDAX"), 0);
    }
}