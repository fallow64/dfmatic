package me.fallow64.builder.blocks.impl;

import me.fallow64.builder.blocks.ArgsTaggable;
import me.fallow64.builder.blocks.CodeBlock;
import me.fallow64.builder.values.CodeValue;
import me.fallow64.builder.values.impl.Tag;

import java.util.List;

public class Repeat extends ArgsTaggable implements CodeBlock {

    private final String action;
    private final String subAction;
    private final boolean inverted;

    public Repeat(String action, String subAction, boolean inverted, List<Tag> tags, List<CodeValue> values) {
        super(tags, values);
        this.action = action;
        this.subAction = subAction;
        this.inverted = inverted;
    }

    public Repeat(String action, List<Tag> tags, List<CodeValue> values) {
        super(tags, values);
        this.action = action;
        this.subAction = null;
        this.inverted = false;
    }

    public String getAction() {
        return action;
    }

    public String getSubAction() {
        return subAction;
    }

    public boolean isInverted() {
        return inverted;
    }

    @Override
    public String serialize() {
        String result = "{\"id\": \"block\",\"block\": \"repeat\"," + getJsonArgs() + ",\"action\": \"" + action + "\"";
        if(subAction != null) {
            result += ",\"subAction\": \"" + subAction + "\"" + (inverted ? ",\"inverted\":\"NOT\"" : "");
        }
        result += "}";
        return result;
    }
}
