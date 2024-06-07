/**
 * AstPrinter.java
 * This file is my version of the Parser for HW3J.
 * Not everything is done "properly," rather it is similar to the solution
 * that I would expect from a student that collaborated with AI.
 * 
 * @author Daniel DeFreez
 * @version Spring 2024
 */

 package edu.sou.cs452.hw3jsolution;

 import java.util.ArrayList;
 import java.util.List;
 
 public class Parser {
     private final List<Token> tokens;
     private int current = 0;
 
     private static class ParseError extends RuntimeException {
     }
 
     public Parser(List<Token> tokens) {
         this.tokens = tokens;
     }
 
     public Expr.Program parse() {
         List<Expr> expressions = new ArrayList<>();
         while (!isAtEnd()) {
             Expr expr = parseDeclaration();
             expressions.add(expr);
         }
         return new Expr.Program(expressions);
     }
 
     private Expr parseDeclaration() {
         if (match(TokenType.ACTOR))
             return actorDeclaration();
         throw error(peek(), "Expected declaration.");
     }
 
     private Expr.Actor actorDeclaration() {
         Token name = consume(TokenType.IDENTIFIER, "Expect actor name.");
         consume(TokenType.NEW, "Expect 'new' keyword for constructor declaration.");
         consume(TokenType.IDENTIFIER, "Expect identifier for constructor.");
         consume(TokenType.LEFT_PAREN, "Expect '(' after 'create'.");
         consume(TokenType.IDENTIFIER, "Expect parameter name.");
         consume(TokenType.COLON, "Expect colon after parameter name.");
 
         // Assuming only one parameter for simplicity
         consume(TokenType.IDENTIFIER, "Expect parameter type.");
         consume(TokenType.RIGHT_PAREN, "Expect ')' after parameters.");
         consume(TokenType.RIGHT_ARROW, "Expect '=>' after constructor declaration.");
 
         List<Expr> bodyExpressions = new ArrayList<>();
 
         while (!isAtEnd()) {
             Expr expr = expression();
             bodyExpressions.add(expr);
         }
 
         return new Expr.Actor(name, bodyExpressions);
     }
 
     private Expr expression() {
         Expr expr;
 
         // This is the initial expression.
         if (match(TokenType.IDENTIFIER)) {
             expr = new Expr.Variable(previous());
         } else if (match(TokenType.STRING)) {
             expr = new Expr.Literal(previous().literal);
         } else {
             throw error(peek(), "Expect expression.");
         }
 
         // Check for method calls or member access, including chaining.
         while (true) {
             if (match(TokenType.DOT)) {
                 Token name = consume(TokenType.IDENTIFIER, "Expect property name after '.'.");
 
                 // Method call
                 if (match(TokenType.LEFT_PAREN)) {
                     List<Expr> arguments = new ArrayList<>();
 
                     // Method arguments
                     if (!check(TokenType.RIGHT_PAREN)) {
                         do {
                             arguments.add(expression());
                         } while (match(TokenType.COMMA));
                     }
                     consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");
                     expr = new Expr.MethodCall(expr, name, arguments);
                 } else {
                     expr = new Expr.MemberAccess(expr, name);
                 }
             } else {
                 break;
             }
         }
 
         return expr;
     }
 
     ///////////////////////////////////////////////////////////////////////////
     // Utility methods
     ///////////////////////////////////////////////////////////////////////////
 
     private boolean match(TokenType... types) {
         for (TokenType type : types) {
             if (check(type)) {
                 advance();
                 return true;
             }
         }
         return false;
     }
 
     private Token consume(TokenType type, String message) {
         if (check(type))
             return advance();
 
         throw error(peek(), message);
     }
 
     private boolean check(TokenType type) {
         if (isAtEnd())
             return false;
         return peek().type == type;
     }
 
     private Token advance() {
         if (!isAtEnd())
             current++;
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
 
     private ParseError error(Token token, String message) {
         App.error(token, message);
         return new ParseError();
     }
 }
 