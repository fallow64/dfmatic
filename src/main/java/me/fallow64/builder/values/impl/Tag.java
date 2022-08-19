package me.fallow64.builder.values.impl;

import me.fallow64.builder.values.CodeValue;
import me.fallow64.dfmatic.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tag implements CodeValue {

    private final String value;
    private final String key;
    private final String action;
    private final String block;

    public Tag(String value, String key, String action, String block) {
        this.value = value;
        this.key = key;
        this.action = action;
        this.block = block;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String getAction() {
        return action;
    }

    public String getBlock() {
        return block;
    }

    @Override
    public String serialize() {
        return "{\"id\": \"bl_tag\",\"data\": {\"option\": \"" + value + "\",\"tag\": \"" + key + "\",\"action\": \"" + action + "\",\"block\": \"" + block + "\"}}";
    }

    public static List<Tag> fromHashmap(HashMap<String, String> hashMap, String action, String block) {
        ArrayList<Tag> result = new ArrayList<>();
        hashMap.forEach((k, v) -> {
            result.add(new Tag(v, k, action, block));
        });
        return result;
    }

    public static List<Tag> fromHashmapFromToken(HashMap<Token, Token> hashMap, String action, String block) {
        ArrayList<Tag> result = new ArrayList<>();
        hashMap.forEach((k, v) -> {
            result.add(new Tag(v.getLexeme(), k.getLexeme(), action, block));
        });
        return result;
    }
}
