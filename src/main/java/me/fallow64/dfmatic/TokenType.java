package me.fallow64.dfmatic;

public enum TokenType {
    // keywords
    EVENT, FUNC,
    VAR, SAVE, GAME, LOCAL,
    IF, NOT, ELSE,
    WHILE, LOOP, IN, FROM, TO, STEP, BREAK, CONTINUE,
    RETURN,
    GAMEVALUE,
    PRINT,

    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACKET, RIGHT_BRACKET,

    SEMICOLON, COLON, COMMA, DOT,

    STAR, SLASH,
    PLUS, MINUS,

    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    IDENTIFIER,
    STRING,
    NUMBER,

    EOF
}
