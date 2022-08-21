package me.fallow64.dfmatic.builder.blocks.impl;

import me.fallow64.dfmatic.builder.blocks.CodeBlock;

public class Else implements CodeBlock {

    @Override
    public String serialize() {
        return "{\"id\": \"block\",\"block\": \"else\"}";
    }

}