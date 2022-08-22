package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public record Text(String value) implements CodeValue {

    public static Text fromColorCode(String value) {
        char[] b = value.toCharArray(); // yoinked directly from spigot
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = '\u00A7';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new Text(new String(b));
    }

    // {"id": "txt","data": {"name": "yo"}}
    @Override
    public String serialize() {
        return "{\"id\": \"txt\",\"data\": {\"name\": \"" + value + "\"}}";
    }
}