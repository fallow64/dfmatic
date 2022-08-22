package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public record Sound(String sound, Double pitch, Double volume) implements CodeValue {

    // {"id": "snd","data": {"sound": "Bass Drum","pitch": 1,"vol": 2}}
    @Override
    public String serialize() {
        return "{\"id\": \"snd\",\"data\": {\"sound\": \"" + sound + "\",\"pitch\": " + pitch + ",\"vol\": " + volume + "}}";
    }

}
