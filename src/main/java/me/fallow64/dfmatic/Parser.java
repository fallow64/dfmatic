package me.fallow64.dfmatic;

import me.fallow64.dfmatic.builder.values.impl.GameValue;
import me.fallow64.dfmatic.ast.Expr;
import me.fallow64.dfmatic.ast.Sect;
import me.fallow64.dfmatic.ast.Stmt;
import me.fallow64.dfmatic.builder.values.impl.Number;
import me.fallow64.dfmatic.builder.values.impl.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Sect> parse() {
        List<Sect> sections = new ArrayList<>();
        while(!isAtEnd()) {
            sections.add(section());
        }

        return sections;
    }

      // ================================================== //
     //                     Section                        //
    // ================================================== //

    private Sect section() {
        try {
            if(match(TokenType.EVENT)) return eventSection();
            if(match(TokenType.FUNC)) return functionSection();

            throw error(peek(), "Expect either event or function section.");
        } catch(ParseError error) {
            synchronize();
        }
        return null;
    }

    private Sect.Event eventSection() {
        Token name = consume(TokenType.STRING, "Expect event name in string.");
        consume(TokenType.LEFT_BRACE, "Expect '{' after event declaration.");
        List<Stmt> body = block();
        return new Sect.Event(name, body);
    }

    private Sect.Function functionSection() {
        Token name = consume(TokenType.IDENTIFIER, "Expect function name.");
        consume(TokenType.LEFT_PAREN, "Expect '(' after function name.");

        List<Token> parameters = parameters();

        consume(TokenType.RIGHT_PAREN, "Expect ')' after function parameters.");

        consume(TokenType.LEFT_BRACE, "Expect '{' before function body.");
        List<Stmt> body = block();

        return new Sect.Function(name, parameters, body);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while(!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(statement());
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

      // ================================================== //
     //                   Statement                        //
    // ================================================== //

    private Stmt statement() { //TODO while, for, return
        if(match(TokenType.VAR)) return varStatement();
        if(match(TokenType.IF)) return ifStatement();
        if(match(TokenType.STRING)) return dfStatement();
        if(match(TokenType.RETURN)) return returnStatement();
        if(match(TokenType.LOOP)) return loopStatement();
        if(match(TokenType.WHILE)) return whileStatement();
        if(match(TokenType.BREAK)) return breakStatement();
        if(match(TokenType.CONTINUE)) return continueStatement();

        return expressionStatement();
    }

    private Stmt loopStatement() { // is this code a mess? kinda
        Expr to = null;
        Expr from = new Expr.Literal(new Number("1.0"));
        Expr step = new Expr.Literal(new Number("1.0"));
        TokenType varType = null;
        Token varName = null;
        if(match(TokenType.LEFT_PAREN)) {

            if(match(TokenType.GAME, TokenType.LOCAL, TokenType.SAVE)) {
                varType = previous().tokenType();
                varName = consume(TokenType.IDENTIFIER, "Expect variable name."); // loop( type varName

                if(match(TokenType.IN)) {
                    Expr list = expression();
                    consume(TokenType.RIGHT_PAREN, "Expect ')' after loop parameters.");
                    consume(TokenType.LEFT_BRACE, "Expect '{' before block.");
                    return new Stmt.LoopFor(varName, varType, list, block());
                }
                if (match(TokenType.FROM)) { // loop( type varName from EXPR to EXPR
                    from = expression();
                    consume(TokenType.TO, "Expect \"to\" after \"from\" expression.");
                    to = expression();
                    if (match(TokenType.STEP)) { // loop ( type? varName from EXPR to EXPR step EXPR
                        step = expression();
                    }
                } else if (match(TokenType.TO)) {
                    to = expression(); // loop ( type varName to EXPR
                    if (match(TokenType.STEP)) { // loop ( type? varName to EXPR step EXPR
                        step = expression();
                    }
                } else {
                    throw error(peek(), "Expect valid loop operation (\"from\" or \"to\")");
                }
            } else {
                to = expression();
            }

            consume(TokenType.RIGHT_PAREN, "Expect ')' after loop parameters.");
            // loop(expression)
        }

        consume(TokenType.LEFT_BRACE, "Expect '{' after loop statement.");

        return new Stmt.Loop(to, from, step, varType, varName, block());
    }

    private Stmt.While whileStatement() {
        boolean inverted = match(TokenType.NOT);
        consume(TokenType.LEFT_PAREN, "Expect '(' after while statement.");

        Expr left = expression();
        Token operator;
        if(match(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            operator = previous();
        } else {
            throw error(peek(), "Expected valid check condition (==, !=, >, >=, < or <=).");
        }
        Expr right = expression();

        consume(TokenType.RIGHT_PAREN, "Expect ')' after while statement.");
        consume(TokenType.LEFT_BRACE, "Expect '{' before while block.");
        return new Stmt.While(left, operator, right, inverted, block());
    }

    private Stmt.Break breakStatement() {
        Stmt.Break stmt = new Stmt.Break(previous());
        consume(TokenType.SEMICOLON, "Expect ';' after break.");
        return stmt;
    }

    private Stmt.Continue continueStatement() {
        Stmt.Continue stmt = new Stmt.Continue(previous());
        consume(TokenType.SEMICOLON, "Expect ';' after continue.");
        return stmt;
    }

    private Stmt.Variable varStatement() {
        Token name = consume(TokenType.IDENTIFIER, "Expect identifier after variable declaration");
        TokenType type = TokenType.LOCAL;
        if(match(TokenType.COLON)) {
            if(match(TokenType.LOCAL, TokenType.GAME, TokenType.SAVE)) {
                type = previous().tokenType();
            } else {
                throw error(peek(), "Expect variable scope.");
            }
        }

        Expr expr = null;
        if(match(TokenType.EQUAL)) {
            expr = expression();
        }

        consume(TokenType.SEMICOLON, "Expect ';' after variable statement.");
        return new Stmt.Variable(name, type, expr);
    }

    private Stmt ifStatement() { // instead of Stmt.If this returns Stmt because it can be either Stmt.If or Stmt.DFIf
        boolean inverted = match(TokenType.NOT);

        if(check(TokenType.STRING)) {
            Token block = consume(TokenType.STRING, "Expect block name in string."); //unreachable error
            consume(TokenType.COLON, "Expect ':' after block name");
            Token action = consume(TokenType.STRING, "Expect action name in string.");

            consume(TokenType.LEFT_PAREN, "Expect '(' before arguments.");
            List<Expr> args = arguments();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");

            consume(TokenType.LEFT_BRACE, "Expect '{' before block.");
            List<Stmt> ifBranch = block();
            List<Stmt> elseBranch = null;
            if (match(TokenType.ELSE)) {
                consume(TokenType.LEFT_BRACE, "Expect '{' before block.");
                elseBranch = block();
            }

            return new Stmt.DFIf(block, action, args, inverted, ifBranch, elseBranch);
        } else {
            consume(TokenType.LEFT_PAREN, "Expect '(' before check.");
            Expr left = expression();
            Token operator;
            if(match(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
                operator = previous();
            } else {
                throw error(peek(), "Expected valid check condition (==, !=, >, >=, < or <=).");
            }
            Expr right = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after check.");

            consume(TokenType.LEFT_BRACE, "Expect '{' before block.");
            List<Stmt> ifBranch = block();
            List<Stmt> elseBranch = null;
            if (match(TokenType.ELSE)) {
                consume(TokenType.LEFT_BRACE, "Expect '{' before block.");
                elseBranch = block();
            }

            return new Stmt.If(left, operator, right, inverted, ifBranch, elseBranch);
        }
    }

    private Stmt.DF dfStatement() {
        Token blockName = previous();
        consume(TokenType.COLON, "Expect ':' after block name.");
        Token actionName = consume(TokenType.STRING, "Expect string after semicolon in DF statement.");

        consume(TokenType.LESS, "Expect '<' after action name.");
        HashMap<Token, Token> tags = tags();
        consume(TokenType.GREATER, "Expect '>' after action tags.");

        consume(TokenType.LEFT_PAREN, "Expect '(' after action tags.");

        List<Expr> arguments = arguments();

        consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");

        consume(TokenType.SEMICOLON, "Expect ';' after DF statement.");

        return new Stmt.DF(blockName, actionName, tags, arguments);
    }

    private Stmt.Return returnStatement() {
        Token keyword = previous();
        Expr value = null;
        if (!check(TokenType.SEMICOLON)) {
            value = expression();
        }

        consume(TokenType.SEMICOLON, "Expect ';' after return value.");
        return new Stmt.Return(keyword, value);
    }

    private Stmt.Expression expressionStatement() {
        Expr expr = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new Stmt.Expression(expr);
    }

      // ================================================== //
     //                    Expression                      //
    // ================================================== //

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = index();

        while(match(TokenType.EQUAL)) {
            Token equals = previous();
            Expr value = expression();

            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable)expr).name;
                expr = new Expr.Assign(name, value);
            } else {
                throw error(equals, "Invalid assignment target.");
            }
        }
        return expr;
    }

    private Expr index() {
        Expr expr = term();
        while(match(TokenType.LEFT_BRACKET)) {
            Expr index = expression();
            expr = new Expr.Index(expr, index);
            consume(TokenType.RIGHT_BRACKET, "Expect ']' after index.");
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while(match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while(match(TokenType.STAR, TokenType.SLASH, TokenType.PERCENT)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if(match(TokenType.MINUS)) {
             Token operator = previous();
             Expr right = primary();
             return new Expr.Unary(operator, right);
        }

        return call();
    }

    private Expr call() {
        Expr expr = primary();

        if(expr instanceof Expr.Variable) {
            if (match(TokenType.LEFT_PAREN)) {
                List<Expr> arguments = arguments();
                consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");
                expr = new Expr.Call(((Expr.Variable) expr).name, arguments);
            }
        }
        if(match(TokenType.DOT)) {
            do {
                if(match(TokenType.STRING, TokenType.IDENTIFIER)) {
                    Token name = previous();
                    expr = new Expr.Get(expr, name);
                } else {
                    throw error(peek(), "Expect property name as identifier or string after '.'.");
                }
            } while (match(TokenType.DOT));
            return expr;
        }
        return expr;
    }

    private Expr primary() {
        if (match(TokenType.NUMBER)) { // literal
            return new Expr.Literal(new Number(previous().literal().toString()));
        } else if(match(TokenType.STRING)) {
            return new Expr.Literal(Text.fromColorCode(previous().literal().toString()));
        } else if(match(TokenType.LEFT_BRACKET)) { // list
            Expr expr = new Expr.Literal(listArgs());
            consume(TokenType.RIGHT_BRACKET, "Expect ']' after list.");
            return expr;
        } else if(match(TokenType.IDENTIFIER)) { // variable
            return new Expr.Variable(previous());
        } else if(match(TokenType.LEFT_BRACE)) { // dictionary
            Expr expr = new Expr.Literal(dictionary());
            consume(TokenType.RIGHT_BRACE, "Expect '}' after list.");
            return expr;
        } else if(match(TokenType.LEFT_PAREN)) { //grouping
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after grouping expression.");
            return new Expr.Grouping(expr);
        } else if(match(TokenType.GAMEVALUE)) {
            return new Expr.Literal(new GameValue((String)consume(TokenType.STRING, "Expect game value type in string.").literal(), "Selection"));
        }

        throw error(peek(), "Expect expression.");
    }

      // ================================================== //
     //                    Util                            //
    // ================================================== //

    private HashMap<Token, Expr> dictionary() {
        HashMap<Token, Expr> result = new HashMap<>();

        if (!check(TokenType.RIGHT_BRACE)) {
            do {
                Token key = consume(TokenType.STRING, "Expect dictionary key in string.");
                consume(TokenType.COLON, "Expect ':' between dictionary key and value.");
                Expr value = expression();
                result.put(key, value);
            } while (match(TokenType.COMMA));
        }

        return result;
    }

    private List<Expr> arguments() {
        List<Expr> arguments = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                arguments.add(expression());
            } while (match(TokenType.COMMA));
        }
        return arguments;
    }

    private List<Token> parameters() {
        List<Token> parameters = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                parameters.add(consume(TokenType.IDENTIFIER, "Expect identifier in parameters."));
            } while (match(TokenType.COMMA));
        }
        return parameters;
    }

    private HashMap<Token, Token> tags() {
        HashMap<Token, Token> tokenMap = new HashMap<>();

        if (!check(TokenType.GREATER)) {
            do {
                Token key = consume(TokenType.STRING, "Expect tag key in string.");
                consume(TokenType.COLON, "Expect ':' between tag key and value.");
                Token value = consume(TokenType.STRING, "Expect tag value in string.");
                tokenMap.put(key, value);
            } while (match(TokenType.COMMA));
        }

        return tokenMap;
    }

    private List<Expr> listArgs() {
        List<Expr> arguments = new ArrayList<>();
        if (!check(TokenType.RIGHT_BRACKET)) {
            do {
                arguments.add(expression());
            } while (match(TokenType.COMMA));
        }
        return arguments;
    }

    private Token advance() {
        if(!isAtEnd()) current++;
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean check(TokenType... types) {
        if(isAtEnd()) return false;
        for (TokenType type : types) {
            if(peek().tokenType() == type) return true;
        }
        return false;
    }

    private boolean match(TokenType... types) {
        if(check(types)) {
            advance();
            return true;
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if(check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean isAtEnd() {
        return peek().tokenType() == TokenType.EOF;
    }

    private ParseError error(Token token, String message) {
        DFMatic.error(token, message);
        throw new ParseError();
    }

    private void synchronize() {
        advance();

        while(!isAtEnd()) {
            if(previous().tokenType() == TokenType.SEMICOLON) return;
            switch(peek().tokenType()) { //TODO implement while and for
                case SAVE, GAME, LOCAL, EVENT, FUNC, VAR, IF -> { return; }
            }

            advance();
        }
    }
}
