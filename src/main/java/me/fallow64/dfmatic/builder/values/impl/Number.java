package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public class Number implements CodeValue {

    private final String value;

    public Number(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // "name" is in string so that it supports %math
    // {"id": "num","data": {"name": "1"}}
    @Override
    public String serialize() {
        return "{\"id\": \"num\",\"data\": {\"name\": \"" + value + "\"}}";
    }
}