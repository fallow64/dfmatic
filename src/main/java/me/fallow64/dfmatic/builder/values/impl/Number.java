package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public record Number(String value) implements CodeValue {

    // "name" is in string so that it supports %math
    // {"id": "num","data": {"name": "1"}}
    @Override
    public String serialize() {
        return "{\"id\": \"num\",\"data\": {\"name\": \"" + value + "\"}}";
    }
}