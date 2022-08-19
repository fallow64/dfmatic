package me.fallow64.builder.blocks.impl;

import me.fallow64.builder.blocks.DataBlock;
import me.fallow64.builder.values.CodeValue;
import me.fallow64.builder.values.impl.Tag;

import java.util.ArrayList;
import java.util.List;

public class StartProcess extends DataBlock {

    public StartProcess(String data, List<Tag> tags) {
        super(data, tags, new ArrayList<>());
    }

    @Override
    public String getBlock() {
        return "start_process";
    }
}
