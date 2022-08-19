package me.fallow64.builder.blocks.impl;

import me.fallow64.builder.blocks.StandardBlock;
import me.fallow64.builder.values.CodeValue;
import me.fallow64.builder.values.impl.Tag;

import java.util.List;

public class EntityAction extends StandardBlock {

    public EntityAction(String action, List<Tag> tags, List<CodeValue> values) {
        super(action, tags, values);
    }

    @Override
    public String getBlock() {
        return "entity_action";
    }

}
