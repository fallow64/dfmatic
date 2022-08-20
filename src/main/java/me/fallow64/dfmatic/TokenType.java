package me.fallow64.dfmatic;

public enum TokenType {
    // keywords
    EVENT, FUNC,
    VAR, SAVE, GAME, LOCAL,
    IF, NOT, ELSE,
    RETURN,
    GAMEVALUE,

    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACKET, RIGHT_BRACKET,

    SEMICOLON, COLON, COMMA, DOT,

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
