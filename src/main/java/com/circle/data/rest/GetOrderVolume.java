package com.circle.data.rest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class GetOrderVolume {

    private double volume;
    private long lastUpdate;

}
