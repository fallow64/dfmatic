package me.fallow64.builder.blocks;

public enum BracketDirection {
    OPEN("open"),
    CLOSE("close");

    private String name;

    BracketDirection(String direction) {
        this.name = direction;
    }

    public String getName() {
        return name;
    }
}
