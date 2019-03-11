package com.circle.storage;

import com.circle.api.DataBuffer;
import com.circle.data.rest.CurrencyPair;
import com.circle.data.rest.GetOrderVolume;
import com.circle.data.rest.OrderSide;
import com.circle.data.websocket.OrderBooks;
import com.circle.data.websocket.OrderUpdate;
import com.circle.exception.NotFoundException;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link DataBuffer} implementation
 */
public class DataBufferImpl implements DataBuffer {

    // initialize to empty in case initialize(...) wasn't called first
    private OrderBooks orderBooks = new OrderBooks(new HashMap<>(), new HashMap<>());

    @Override
    public GetOrderVolume lookup(CurrencyPair pair, double price, OrderSide orderSide) throws NotFoundException, IllegalArgumentException {
        System.out.println(orderBooks);
        Preconditions.checkArgument(CurrencyPair.BTC_ETH == pair,
                String.format("Currency pair %s is not supported.", pair));
        switch (orderSide) {
            case ASK:
                if (orderBooks.getAsk().containsKey(price)) {
                    return orderBooks.getAsk().get(price);
                } else {
                    throw new NotFoundException("Price level not found in ask book: " + price);
                }
            case BID:
                if (orderBooks.getBid().containsKey(price)) {
                    return orderBooks.getBid().get(price);
                } else {
                    throw new NotFoundException("Price level not found in bid book: " + price);
                }
            default:
                throw new IllegalArgumentException("Invalid order side: " + orderSide);
        }
    }

    @Override
    public void initialize(OrderBooks initialDump) {
        orderBooks = initialDump;
    }

    @Override
    public void update(OrderUpdate orderUpdate) {
        switch (orderUpdate.getOrderSide()) {
            case ASK:
                updateBook(orderUpdate, orderBooks.getAsk());
                break;
            case BID:
                updateBook(orderUpdate, orderBooks.getBid());
                break;
            default:
                throw new IllegalArgumentException("Invalid order side: " + orderUpdate.getOrderSide());
        }
    }

    private void updateBook(OrderUpdate orderUpdate, Map<Double, GetOrderVolume> book) {
        if (!equalsZero(orderUpdate.getVolume())) {
            book.put(orderUpdate.getPrice(), new GetOrderVolume(orderUpdate.getVolume(), orderUpdate.getUpdateTime()));
        } else {
            book.remove(orderUpdate.getPrice());
        }
    }

    private boolean equalsZero(double val) {
        return Math.abs(val) < 2 * Double.MIN_VALUE;
    }
}
