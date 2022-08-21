package me.fallow64.dfmatic.builder.blocks.impl;

import me.fallow64.dfmatic.builder.blocks.BracketDirection;
import me.fallow64.dfmatic.builder.blocks.CodeBlock;

public class Bracket implements CodeBlock {

    private final BracketDirection direction;

    public Bracket(BracketDirection direction) {
        this.direction = direction;
    }

    public BracketDirection getDirection() {
        return direction;
    }

    @Override
    public String serialize() {
        return "{\"id\":\"bracket\",\"direct\":\"" + direction.getName() + "\",\"type\":\"norm\"}";
    }
}
