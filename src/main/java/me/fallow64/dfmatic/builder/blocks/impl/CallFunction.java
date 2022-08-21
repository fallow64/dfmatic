package me.fallow64.dfmatic.builder.blocks.impl;

import me.fallow64.dfmatic.builder.blocks.DataBlock;

import java.util.ArrayList;

public class CallFunction extends DataBlock {

    public CallFunction(String data) {
        super(data, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public String getBlock() {
        return "call_func";
    }
}
