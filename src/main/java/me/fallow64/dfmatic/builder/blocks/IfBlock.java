package me.fallow64.dfmatic.builder.blocks;

import me.fallow64.dfmatic.builder.values.CodeValue;
import me.fallow64.dfmatic.builder.values.impl.Tag;

import java.util.List;

public abstract class IfBlock extends ArgsTaggable implements CodeBlock {

    private final String action;
    private final boolean inverted;

    public IfBlock(String action, boolean inverted, List<Tag> tags, List<CodeValue> values) {
        super(tags, values);
        this.action = action;
        this.inverted = inverted;
    }

    public String getAction() {
        return action;
    }

    public boolean isInverted() {
        return inverted;
    }

    public abstract String getBlock();

    @Override
    public String serialize() {
        return "{\"id\": \"block\",\"block\": \"" + getBlock() + "\"," + getJsonArgs() + ",\"action\": \"" + action + "\"" + (inverted ? ",\"inverted\":\"NOT\"" : "") + "}";
    }
}
