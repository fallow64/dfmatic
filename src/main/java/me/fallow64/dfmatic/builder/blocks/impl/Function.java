package me.fallow64.dfmatic.builder.blocks.impl;

import me.fallow64.dfmatic.builder.blocks.CodeHeader;
import me.fallow64.dfmatic.builder.blocks.DataBlock;
import me.fallow64.dfmatic.builder.values.impl.Tag;

import java.util.ArrayList;
import java.util.List;

public class Function extends DataBlock implements CodeHeader {

    public Function(String data, List<Tag> tags) {
        super(data, tags, new ArrayList<>());
    }

    @Override
    public String getBlock() {
        return "func";
    }

    @Override
    public String getTemplateName() {
        return "Function \u00BB " + getData();
    }

    @Override
    public String getTemplateNameWithColors() {
        return "\u00A7b\u00A7lFunction \u00A73\u00BB \u00A7b" + getData();
    }

}
