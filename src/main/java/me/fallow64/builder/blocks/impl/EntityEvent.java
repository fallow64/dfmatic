package me.fallow64.builder.blocks.impl;

import me.fallow64.builder.blocks.CodeHeader;
import me.fallow64.builder.blocks.StandardBlock;

import java.util.ArrayList;

public class EntityEvent extends StandardBlock implements CodeHeader {

    public EntityEvent(String data) {
        super(data, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public String getBlock() {
        return "entity_event";
    }

    @Override
    public String getTemplateName() {
        return "Event » " + getAction();
    }
}
