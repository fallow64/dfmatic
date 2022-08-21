package me.fallow64.dfmatic.builder.blocks.impl;

import me.fallow64.dfmatic.builder.blocks.IfBlock;
import me.fallow64.dfmatic.builder.values.CodeValue;
import me.fallow64.dfmatic.builder.values.impl.Tag;

import java.util.List;

public class IfEntity extends IfBlock {

    public IfEntity(String action, boolean inverted, List<Tag> tags, List<CodeValue> values) {
        super(action, inverted, tags, values);
    }

    @Override
    public String getBlock() {
        return "if_entity";
    }
}
