package me.fallow64.builder.blocks;

import me.fallow64.builder.values.CodeValue;
import me.fallow64.builder.values.impl.Tag;

import java.util.List;

public abstract class DataBlock extends ArgsTaggable implements CodeBlock {

    private final String data;

    public DataBlock(String data, List<Tag> tags, List<CodeValue> values) {
        super(tags, values);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public abstract String getBlock();

    @Override
    public String serialize() {
        return "{\"id\": \"block\",\"block\": \"" + getBlock() + "\"," + getJsonArgs() + ",\"data\": \"" + data + "\"}";
    }
}