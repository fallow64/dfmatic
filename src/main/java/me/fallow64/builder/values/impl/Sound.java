package me.fallow64.builder.values.impl;

import me.fallow64.builder.values.CodeValue;

public class Sound implements CodeValue {

    private final String sound;
    private final Double pitch;
    private final Double volume;

    public Sound(String sound, Double pitch, Double volume) {
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }

    public String getSound() {
        return sound;
    }

    public Double getPitch() {
        return pitch;
    }

    public Double getVolume() {
        return volume;
    }

    // {"id": "snd","data": {"sound": "Bass Drum","pitch": 1,"vol": 2}}
    @Override
    public String serialize() {
        return "{\"id\": \"snd\",\"data\": {\"sound\": \"" + sound + "\",\"pitch\": " + pitch + ",\"vol\": " + volume + "}}";
    }

}
