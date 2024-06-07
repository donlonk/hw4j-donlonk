package edu.sou.cs452.hw3j;

public enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, DOT, PLUS, EQUAL, COLON, GREATER, LESS,

    // One or two character tokens.
    BANG,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    PRINT, LET, TRUE, FALSE, ACTOR, NEW, ENV,

    EOF
}
