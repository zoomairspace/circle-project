package com.circle.guice;

import com.circle.api.DataBuffer;
import com.circle.storage.DataBufferImpl;
import com.circle.websocket.DataPoller;
import com.circle.websocket.DataPollerTask;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.common.scopes.SimpleContainerScope;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CircleModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public DataBuffer getDataBuffer() {
        return new DataBufferImpl();
    }

    @Provides
    @Singleton
    public ExecutorService getDataPollerExecutor(DataPollerTask task) {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        return executor;
    }

    @Provides
    @Singleton
    private WebSocketClient getWebSocketClient() {
        final WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.CLIENT);
        // "i" message exceeds default size 65536
        policy.setMaxTextMessageSize(1000000);
        return new WebSocketClient(new SimpleContainerScope(policy));
    }

    @Provides
    @Singleton
    private DataPoller getDataPoller(DataBuffer dataBuffer) {
        return new DataPoller(dataBuffer);
    }

    @Provides
    @Singleton
    private DataPollerTask getDataPollerTask(WebSocketClient client, DataPoller dataPoller) {
        return new DataPollerTask(client, dataPoller);
    }
}
