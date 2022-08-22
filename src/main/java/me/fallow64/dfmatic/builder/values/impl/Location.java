package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public record Location(Double x, Double y, Double z, Double pitch, Double yaw) implements CodeValue {

    // isBlock is always set to false (confirmed by Daed)
    // {"id": "loc","data": {"isBlock": false, "loc": {"x": 2.5,"y": 49.5,"z": 6.5,"pitch": 0,"yaw": 0}}}
    @Override
    public String serialize() {
        return "{\"id\": \"loc\",\"data\": {\"isBlock\": false, \"loc\": {\"x\": " + x + ",\"y\": " + y + ",\"z\": " + z + ",\"pitch\": " + pitch + ",\"yaw\": " + yaw + "}}}";
    }

}
