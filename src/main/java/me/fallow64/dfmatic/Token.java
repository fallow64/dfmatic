package me.fallow64.dfmatic;

public class Token {


    private final TokenType tokenType;
    private final String lexeme;
    private final int lineNumber;
    private final Object literal;

    public Token(TokenType tokenType, String lexeme, int lineNumber, Object literal) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.literal = literal;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Object getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return "{" + tokenType.name() + ", '" + lexeme + "', " + lineNumber + ", " + literal + "}";
    }

}
