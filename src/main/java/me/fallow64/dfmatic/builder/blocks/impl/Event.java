package me.fallow64.dfmatic.builder.blocks.impl;

import me.fallow64.dfmatic.builder.blocks.CodeHeader;
import me.fallow64.dfmatic.builder.blocks.StandardBlock;

import java.util.ArrayList;

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
        return "Event \u00BB " + getAction();
    }

    @Override
    public String getTemplateNameWithColors() {
        return "\u00A7e\u00A7lEvent \u00BB \u00A7e" + getAction() + " Event";
    }

}
