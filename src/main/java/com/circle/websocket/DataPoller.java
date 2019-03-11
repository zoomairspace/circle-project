package com.circle.websocket;

import com.circle.api.DataBuffer;
import com.circle.data.rest.GetOrderVolume;
import com.circle.data.rest.OrderSide;
import com.circle.data.websocket.OrderBooks;
import com.circle.data.websocket.OrderUpdate;
import com.google.common.base.Preconditions;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket
 */
@WebSocket
public class DataPoller {

    private static final String WS_REQUEST = "{\"command\": \"subscribe\", \"channel\": \"BTC_ETH\"}";

    private final DataBuffer dataBuffer;
    private Session session;

    @Inject
    public DataPoller(DataBuffer dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        System.out.println("Connected to server");
        sendMessage(session, WS_REQUEST);
    }

    @OnWebSocketMessage
    public void receiveMessage(Session session, String message) throws IOException {
        System.out.println("Message received from server: " + message);
        // parse message and update DataBuffer
        parseAndUpdateDataBuffer(message);
    }

    /**
     * See https://github.com/zoomairspace/circle-project/blob/master/DesignDoc.md#receive-data for message structures.
     */
    private void parseAndUpdateDataBuffer(String message) {
        final JSONArray rawMessage = new JSONArray(message);
        if (rawMessage.getInt(0) == 1010) {
            // skip heartbeat
            return;
        }
        final JSONArray orderBookUpdates = rawMessage.getJSONArray(2);
        final long currentTimeMilli = Instant.now().toEpochMilli(); // defaults to UTC
        for (int i = 0; i < orderBookUpdates.length(); i++) {
            final JSONArray update = orderBookUpdates.getJSONArray(i);
            final String updateType = update.getString(0).toLowerCase();
            switch (updateType) {
                case "o":
                    Preconditions.checkArgument(update.length() == 4);
                    dataBuffer.update(new OrderUpdate(
                                    update.getDouble(2),
                                    update.getDouble(3),
                                    update.getInt(1) == 0 ? OrderSide.ASK : OrderSide.BID,
                                    currentTimeMilli));
                    break;
                case "i":
                    Preconditions.checkArgument(update.length() == 2);
                    final JSONArray orderBooks = update.getJSONObject(1).getJSONArray("orderBook");
                    dataBuffer.initialize(new OrderBooks(
                                buildOrderBook(orderBooks.getJSONObject(0), currentTimeMilli),
                                buildOrderBook(orderBooks.getJSONObject(1), currentTimeMilli)));
                    break;
                default:
                    // ignore other types such as "t"
            }
        }
    }

    private Map<Double, GetOrderVolume> buildOrderBook(JSONObject jsonOrderBook, long lastUpdateTimestamp) {
        final Map<Double, GetOrderVolume> newOrderBook = new HashMap<>();
        for (Map.Entry<String, Object> bookEntry : jsonOrderBook.toMap().entrySet()) {
            newOrderBook.put(
                    Double.parseDouble(bookEntry.getKey()),
                    new GetOrderVolume(Double.parseDouble(bookEntry.getValue().toString()), lastUpdateTimestamp));
        }
        return newOrderBook;
    }

    private void sendMessage(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
