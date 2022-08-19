package me.fallow64.builder.values.impl;

import me.fallow64.builder.values.CodeValue;

public class Text implements CodeValue {

    private final String value;

    public Text(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // "name" is in string so that it supports %math
    // {"id": "num","data": {"name": "1"}}
    @Override
    public String serialize() {
        return "{\"id\": \"txt\",\"data\": {\"name\": \"" + value + "\"}}";
    }
}