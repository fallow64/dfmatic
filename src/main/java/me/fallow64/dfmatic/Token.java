package me.fallow64.dfmatic;

import me.fallow64.dfmatic.util.Pair;

public record Token(TokenType tokenType, String lexeme, Pair<Integer, Integer> location, Object literal) {}
