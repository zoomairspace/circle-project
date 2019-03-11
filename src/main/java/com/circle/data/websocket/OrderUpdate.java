package com.circle.data.websocket;

import com.circle.data.rest.OrderSide;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class OrderUpdate {

    private double price;
    private double volume;
    private OrderSide orderSide;
    private long updateTime;

}
