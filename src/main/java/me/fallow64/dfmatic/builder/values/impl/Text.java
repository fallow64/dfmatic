package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;

public class Text implements CodeValue {

    private final String value;

    public Text(String value) {
        this.value = value;
    }

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

    public String getValue() {
        return value;
    }

    // {"id": "txt","data": {"name": "yo"}}
    @Override
    public String serialize() {
        return "{\"id\": \"txt\",\"data\": {\"name\": \"" + value + "\"}}";
    }
}