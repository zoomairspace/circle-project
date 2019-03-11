package com.circle.api;

import com.circle.data.rest.CurrencyPair;
import com.circle.data.rest.GetOrderVolume;
import com.circle.data.rest.OrderSide;
import com.circle.data.websocket.OrderBooks;
import com.circle.data.websocket.OrderUpdate;
import com.circle.exception.NotFoundException;

/**
 *  data buffer interface
 */
public interface DataBuffer {

    /**
     * Lookup the order volume.
     * @param pair
     * @param price
     * @param side
     * @return
     * @throws NotFoundException
     * @throws IllegalArgumentException
     */
    GetOrderVolume lookup(CurrencyPair pair, double price, OrderSide side) throws NotFoundException, IllegalArgumentException;

    /**
     * Initialize received data buffer
     * @param initialDump
     */
    void initialize(OrderBooks initialDump);

    /**
     * Update data buffer
     * @param orderUpdate
     */
    void update(OrderUpdate orderUpdate);
}
