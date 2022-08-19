package me.fallow64.builder.blocks;

import me.fallow64.builder.values.CodeValue;
import me.fallow64.builder.values.impl.Tag;

import java.util.List;

public abstract class StandardBlock extends ArgsTaggable implements CodeBlock {

    private final String action;

    public StandardBlock(String action, List<Tag> tags, List<CodeValue> values) {
        super(tags, values);
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public abstract String getBlock();

    @Override
    public String serialize() {
        return "{\"id\": \"block\",\"block\": \"" + getBlock() + "\"," + getJsonArgs() + ",\"action\": \"" + action + "\"}";
    }
}
