package me.fallow64.dfmatic.builder.blocks;

import me.fallow64.dfmatic.builder.values.CodeValue;
import me.fallow64.dfmatic.builder.values.impl.Tag;

import java.util.ArrayList;
import java.util.List;

public class ArgsTaggable extends Taggable {

    private final List<CodeValue> values;

    public ArgsTaggable(List<Tag> tags, List<CodeValue> values) {
        super(tags);
        this.values = values;
    }

    @Override
    public List<String> getArgs() {
        List<String> result = new ArrayList<>();
        for(int i = 0; i < values.size(); i++) {
            result.add("{\"item\":" + values.get(i).serialize() + ",\"slot\":" + i + "}");
        }
        result.addAll(super.getArgs());
        return result;
    }
}
