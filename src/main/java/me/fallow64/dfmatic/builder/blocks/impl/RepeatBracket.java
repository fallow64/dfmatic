package me.fallow64.dfmatic.builder.blocks.impl;

import me.fallow64.dfmatic.builder.blocks.BracketDirection;
import me.fallow64.dfmatic.builder.blocks.CodeBlock;

public class RepeatBracket implements CodeBlock {

    private final BracketDirection direction;

    public RepeatBracket(BracketDirection bracketDirection) {
        this.direction = bracketDirection;
    }

    public BracketDirection getDirection() {
        return direction;
    }

    @Override
    public String serialize() {
        return "{\"id\":\"bracket\",\"direct\":\"" + direction.getName()  + "\",\"type\":\"repeat\"}";
    }
}
