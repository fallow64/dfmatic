package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;
import me.fallow64.dfmatic.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public record Tag(String value, String key, String action, String block) implements CodeValue {

    @Override
    public String serialize() {
        return "{\"id\": \"bl_tag\",\"data\": {\"option\": \"" + value + "\",\"tag\": \"" + key + "\",\"action\": \"" + action + "\",\"block\": \"" + block + "\"}}";
    }

    public static List<Tag> fromHashmap(HashMap<String, String> hashMap, String action, String block) {
        ArrayList<Tag> result = new ArrayList<>();
        hashMap.forEach((k, v) -> result.add(new Tag(v, k, action, block)));
        return result;
    }

    public static List<Tag> fromHashmapFromToken(HashMap<Token, Token> hashMap, String action, String block) {
        ArrayList<Tag> result = new ArrayList<>();
        hashMap.forEach((k, v) -> result.add(new Tag((String) v.literal(), (String) k.literal(), action, block)));
        return result;
    }
}
