package com.circle.websocket;

import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.inject.Inject;
import java.net.URI;

public class DataPollerTask implements Runnable {

    private static final String WS_ENDPOINT = "wss://api2.poloniex.com";

    private final WebSocketClient client;
    private final DataPoller dataPoller;

    @Inject
    public DataPollerTask(WebSocketClient client, DataPoller dataPoller) {
        this.client = client;
        this.dataPoller = dataPoller;
    }

    @Override
    public void run() {
        try {
            client.start();
            client.connect(dataPoller, new URI(WS_ENDPOINT));
            client.setStopAtShutdown(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
