package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;
import me.fallow64.dfmatic.builder.values.VariableScope;

public record Variable(String name, VariableScope scope) implements CodeValue {

    // scope is either unsaved, saved, or local
    // {"id": "var","data": {"name": "exampleGameVar","scope": "unsaved"}}
    @Override
    public String serialize() {
        return "{\"id\": \"var\",\"data\": {\"name\": \"" + name + "\", \"scope\": \"" + scope.getScopeName() + "\"}}";
    }

}