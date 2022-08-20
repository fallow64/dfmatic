package me.fallow64.builder.values.impl;

import me.fallow64.builder.values.CodeValue;

public class GameValue implements CodeValue {

    private final String type;
    private final String target;

    public GameValue(String type) {
        this.type = type;
        this.target = "Selection";
    }

    public GameValue(String type, String target) {
        this.type = type;
        this.target = target;
    }

    @Override
    public String serialize() {
        return "{\"id\": \"g_val\",\"data\": {\"type\": \"" + type + "\",\"target\": \"" + target + "\"}}";
    }
}