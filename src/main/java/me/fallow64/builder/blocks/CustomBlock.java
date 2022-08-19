package me.fallow64.builder.blocks;

import me.fallow64.builder.values.CodeValue;
import me.fallow64.builder.values.impl.Tag;

import java.util.List;

public class CustomBlock extends ArgsTaggable implements CodeBlock {

    private final String block;
    private final String action;

    public CustomBlock(String block, String action, List<Tag> tags, List<CodeValue> values) {
        super(tags, values);
        this.block = block;
        this.action = action;
    }

    public String getBlock() {
        return block;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String serialize() {
        return "{\"id\": \"block\",\"block\": \"" + block +
                    "\"," + getJsonArgs() + ",\"action\": \"" + action + "\"}";
    }
}
