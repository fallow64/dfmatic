package me.fallow64.dfmatic.builder.values.impl;

import me.fallow64.dfmatic.builder.values.CodeValue;
import me.fallow64.dfmatic.builder.values.VariableScope;

public class Variable implements CodeValue {

    private final String name;
    private final VariableScope scope;

    public Variable(String name, VariableScope type) {
        this.name = name;
        this.scope = type;
    }

    public String getName() {
        return name;
    }

    public VariableScope getScope() {
        return scope;
    }

    // scope is either unsaved, saved, or local
    // {"id": "var","data": {"name": "exampleGameVar","scope": "unsaved"}}
    @Override
    public String serialize() {
        return "{\"id\": \"var\",\"data\": {\"name\": \"" + name + "\", \"scope\": \"" + scope.getScopeName() + "\"}}";
    }

}