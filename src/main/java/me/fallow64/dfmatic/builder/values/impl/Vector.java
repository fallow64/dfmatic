package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public record Vector(Double x, Double y, Double z) implements CodeValue {

    // {"id": "vec","data": {"x": 1.1,"y": 2.2,"z": 3.3}}
    @Override
    public String serialize() {
        return "{\"id\": \"vec\",\"data\": {\"x\": " + x + ",\"y\": " + y + ",\"z\": " + z + "}}";
    }

}