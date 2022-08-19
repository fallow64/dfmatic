package me.fallow64.builder.blocks.impl;

import me.fallow64.builder.blocks.CodeHeader;
import me.fallow64.builder.blocks.DataBlock;
import me.fallow64.builder.values.impl.Tag;

import java.util.ArrayList;
import java.util.List;

public class Function extends DataBlock implements CodeHeader {

    public Function(String data, List<Tag> tags) {
        super(data, tags, new ArrayList<>());
    }

    @Override
    public String getBlock() {
        return "func";
    }

    @Override
    public String getTemplateName() {
        return "Function » " + getData();
    }

}
