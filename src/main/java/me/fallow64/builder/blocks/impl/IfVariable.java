package me.fallow64.builder.blocks.impl;

import me.fallow64.builder.blocks.IfBlock;
import me.fallow64.builder.values.CodeValue;
import me.fallow64.builder.values.impl.Tag;

import java.util.List;

public class IfVariable extends IfBlock {

    public IfVariable(String action, boolean inverted, List<Tag> tags, List<CodeValue> values) {
        super(action, inverted, tags, values);
    }

    @Override
    public String getBlock() {
        return "if_var";
    }
}
