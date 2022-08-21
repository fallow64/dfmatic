package me.fallow64.dfmatic.builder.blocks.impl;

import me.fallow64.dfmatic.builder.blocks.StandardBlock;
import me.fallow64.dfmatic.builder.values.CodeValue;
import me.fallow64.dfmatic.builder.values.impl.Tag;

import java.util.List;

public class PlayerAction extends StandardBlock {

    public PlayerAction(String action, List<Tag> tags, List<CodeValue> values) {
        super(action, tags, values);
    }

    @Override
    public String getBlock() {
        return "player_action";
    }

}
