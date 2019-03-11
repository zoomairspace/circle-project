package com.circle.api;

import com.circle.data.rest.CurrencyPair;
import com.circle.data.rest.GetOrderVolume;
import com.circle.data.rest.OrderSide;
import com.circle.data.websocket.OrderBooks;
import com.circle.data.websocket.OrderUpdate;
import com.circle.exception.NotFoundException;

public interface DataBuffer {

    GetOrderVolume lookup(CurrencyPair pair, double price, OrderSide side) throws NotFoundException, IllegalArgumentException;

    void initialize(OrderBooks initialDump);

    void update(OrderUpdate orderUpdate);
}
