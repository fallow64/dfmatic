package me.fallow64.dfmatic.builder.values;

public enum VariableScope {
    GAME("unsaved"),
    SAVE("saved"),
    LOCAL("local");

    private final String scopeName;

    VariableScope(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getScopeName() {
            return scopeName;
        }
}
