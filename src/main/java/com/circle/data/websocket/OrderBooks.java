package com.circle.data.websocket;

import com.circle.data.rest.GetOrderVolume;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class OrderBooks {

    private Map<Double, GetOrderVolume> ask;
    private Map<Double, GetOrderVolume> bid;

}
