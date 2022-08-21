package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public class Location implements CodeValue {

    private final Double x;
    private final Double y;
    private final Double z;
    private final Double pitch;
    private final Double yaw;

    public Location(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = 0.0;
        this.yaw = 0.0;
    }

    public Location(Double x, Double y, Double z, Double pitch, Double yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
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

    public Double getPitch() {
        return pitch;
    }

    public Double getYaw() {
        return yaw;
    }

    // isBlock is always set to false (confirmed by Daed)
    // {"id": "loc","data": {"isBlock": false, "loc": {"x": 2.5,"y": 49.5,"z": 6.5,"pitch": 0,"yaw": 0}}}
    @Override
    public String serialize() {
        return "{\"id\": \"loc\",\"data\": {\"isBlock\": false, \"loc\": {\"x\": " + x + ",\"y\": " + y + ",\"z\": " + z + ",\"pitch\": " + pitch + ",\"yaw\": " + yaw + "}}}";
    }

}
