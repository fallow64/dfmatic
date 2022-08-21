package me.fallow64.dfmatic.builder.blocks;

import me.fallow64.dfmatic.builder.values.impl.Tag;

import java.util.ArrayList;
import java.util.List;

public class Taggable {

    private final List<Tag> tags;

    public Taggable(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<String> getArgs() {
        List<String> result = new ArrayList<>();
        // keeps the order of the original tags from left to right
        for(int i = 0; i < tags.size(); i++) {
            result.add("{\"item\":" + tags.get(i).serialize() + ",\"slot\":" + (i + 26 - tags.size() + 1) + "}");
        }
        return result;
    }

    public String getJsonArgs() {
        return "\"args\": {\"items\": [" + String.join(",", getArgs()) + "]}";
    }

}