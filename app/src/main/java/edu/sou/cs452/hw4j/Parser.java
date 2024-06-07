package edu.sou.cs452.hw4j;

import java.util.List;
import java.util.ArrayList;

public class Parser {
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt declaration() {
        try {
            if (match(TokenType.LET)) return letDeclaration();
            if (match(TokenType.ACTOR)) return actorDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt letDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
        consume(TokenType.COLON, "Expect ':' after variable name.");
        Token type = consume(TokenType.IDENTIFIER, "Expect type name.");

        Expr initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }
        consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new Stmt.Var(name, initializer);
    }

    private Stmt actorDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect actor name.");
        consume(TokenType.LEFT_BRACE, "Expect '{' before actor body.");

        List<Stmt> body = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            if (match(TokenType.NEW)) {
                body.add(newDeclaration());
            } else {
                body.add(declaration());
            }
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after actor body.");
        return new Stmt.Actor(name, body);
    }

    private Stmt newDeclaration() {
        Token createToken = consume(TokenType.IDENTIFIER, "Expect 'create' after 'new'.");
        if (!createToken.lexeme.equals("create")) {
            throw error(createToken, "Expect 'create' after 'new'.");
        }

        consume(TokenType.LEFT_PAREN, "Expect '(' after 'create'.");
        Token env = consume(TokenType.IDENTIFIER, "Expect 'env' as parameter.");
        consume(TokenType.COLON, "Expect ':' after 'env'.");
        Token envType = consume(TokenType.IDENTIFIER, "Expect type name.");
        consume(TokenType.RIGHT_PAREN, "Expect ')' after parameter.");
        consume(TokenType.EQUAL, "Expect '=>' after ')'.");
        Stmt.Block block = blockStatement();
        return new Stmt.New(env, block);
    }

    private Stmt statement() {
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.LEFT_BRACE)) return blockStatement();

        return expressionStatement();
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new Stmt.Expression(expr);
    }

    private Stmt.Block blockStatement() {
        List<Stmt> statements = new ArrayList<>();

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return new Stmt.Block(statements);
    }

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = addition();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = addition();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr addition() {
        Expr expr = multiplication();
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = multiplication();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr multiplication() {
        Expr expr = unary();
        while (match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.FALSE)) return new Expr.Literal(false);
        if (match(TokenType.TRUE)) return new Expr.Literal(true);
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expr.Literal(previous().literal);
        }
        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }
        throw error(peek(), "Expect expression.");
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        System.err.println("Error at " + token.lexeme + ": " + message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return;
            switch (peek().type) {
                case LEFT_PAREN:
                case RIGHT_PAREN:
                case LEFT_BRACE:
                case RIGHT_BRACE:
                case COMMA:
                case DOT:
                case MINUS:
                case PLUS:
                case SEMICOLON:
                case SLASH:
                case STAR:
                case COLON:
                case BANG:
                case BANG_EQUAL:
                case EQUAL:
                case EQUAL_EQUAL:
                case GREATER:
                case GREATER_EQUAL:
                case LESS:
                case LESS_EQUAL:
                case IDENTIFIER:
                case STRING:
                case NUMBER:
                case FALSE:
                case TRUE:
                case PRINT:
                case LET:
                case ACTOR:
                case NEW:
                case ENV:
                    return;
            }
            advance();
        }
    }
}

