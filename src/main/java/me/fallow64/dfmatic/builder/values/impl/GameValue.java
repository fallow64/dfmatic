package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public class GameValue implements CodeValue {

    private final String type;
    private final String target;

    public GameValue(String type, String target) {
        this.type = type;
        this.target = target;
    }

    @Override
    public String serialize() {
        return "{\"id\": \"g_val\",\"data\": {\"type\": \"" + type + "\",\"target\": \"" + target + "\"}}";
    }
}