package edu.sou.cs452.hw4j;

import java.util.HashMap;
import java.util.Map;

public class Environment<T> {
    private final Map<String, T> values = new HashMap<>();
    private final Map<String, String> types = new HashMap<>();
    private final Environment<T> enclosing;

    public Environment() {
        enclosing = null;
    }

    public Environment(Environment<T> enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, T value, String type) {
        values.put(name, value);
        types.put(name, type);
    }

    public T get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    public String getType(Token name) {
        if (types.containsKey(name.lexeme)) {
            return types.get(name.lexeme);
        }

        if (enclosing != null) return enclosing.getType(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    public void assign(Token name, T value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    public void assignType(Token name, String type) {
        if (types.containsKey(name.lexeme)) {
            types.put(name.lexeme, type);
            return;
        }

        if (enclosing != null) {
            enclosing.assignType(name, type);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }
}
