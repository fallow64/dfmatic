package me.fallow64.builder.blocks.impl;

import me.fallow64.builder.blocks.CodeHeader;
import me.fallow64.builder.blocks.StandardBlock;
import me.fallow64.builder.values.CodeValue;

import java.util.ArrayList;
import java.util.List;

public class Event extends StandardBlock implements CodeHeader {

    public Event(String data) {
        super(data, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public String getBlock() {
        return "event";
    }

    @Override
    public String getTemplateName() {
        return "Event » " + getAction();
    }
}
