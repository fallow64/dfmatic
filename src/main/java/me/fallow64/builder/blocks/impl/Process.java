package me.fallow64.builder.blocks.impl;

import me.fallow64.builder.blocks.CodeHeader;
import me.fallow64.builder.blocks.DataBlock;
import me.fallow64.builder.values.impl.Tag;

import java.util.ArrayList;
import java.util.List;

public class Process extends DataBlock implements CodeHeader {

    public Process(String data, List<Tag> tags) {
        super(data, tags, new ArrayList<>());
    }

    @Override
    public String getBlock() {
        return "process";
    }

    @Override
    public String getTemplateName() {
        return "Process » " + getData();
    }

}
