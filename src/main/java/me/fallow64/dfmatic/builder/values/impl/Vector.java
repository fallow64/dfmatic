package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public class Vector implements CodeValue {

    private final Double x;
    private final Double y;
    private final Double z;

    public Vector(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    // {"id": "vec","data": {"x": 1.1,"y": 2.2,"z": 3.3}}
    @Override
    public String serialize() {
        return "{\"id\": \"vec\",\"data\": {\"x\": " + x + ",\"y\": " + y + ",\"z\": " + z + "}}";
    }

}