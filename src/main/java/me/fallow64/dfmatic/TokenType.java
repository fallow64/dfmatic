package me.fallow64.dfmatic;

public enum TokenType {
    EVENT, FUNC,
    VAR, SAVE, GAME, LOCAL,
    IF, NOT, ELSE,
    RETURN,

    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,

    SEMICOLON, COLON, COMMA,

    PLUS, MINUS,
    STAR, SLASH,

    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    IDENTIFIER,
    STRING,
    NUMBER,

    EOF
}
