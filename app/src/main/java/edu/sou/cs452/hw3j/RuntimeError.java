package edu.sou.cs452.hw3j;

public class RuntimeError extends RuntimeException {
    final Token token;

    RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}
