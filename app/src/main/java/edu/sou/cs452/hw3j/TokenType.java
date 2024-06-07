package edu.sou.cs452.hw3j;

enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, COLON,
    DOT, COMMA,

    // One or two character tokens.
    RIGHT_ARROW,

    // Literals.
    IDENTIFIER, STRING,

    // Keywords.
    ACTOR, NEW,

    EOF
}
