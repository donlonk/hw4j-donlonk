package edu.sou.cs452.hw3j;

import java.util.List;

abstract class Stmt {
    interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);
        R visitPrintStmt(Print stmt);
        R visitVarStmt(Var stmt);
        R visitBlockStmt(Block stmt);
        R visitActorStmt(Actor stmt); // Added
        R visitNewStmt(New stmt);     // Added
    }

    static class Expression extends Stmt {
        final Expr expression;

        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class Print extends Stmt {
        final Expr expression;

        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    static class Var extends Stmt {
        final Token name;
        final Expr initializer;

        Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    static class Block extends Stmt {
        final List<Stmt> statements;

        Block(List<Stmt> statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }
    }

    static class Actor extends Stmt { // Added
        final Token name;
        final List<Stmt> body;

        Actor(Token name, List<Stmt> body) {
            this.name = name;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitActorStmt(this);
        }
    }

    static class New extends Stmt { // Added
        final Token env;
        final Block block;

        New(Token env, Block block) {
            this.env = env;
            this.block = block;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitNewStmt(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}
