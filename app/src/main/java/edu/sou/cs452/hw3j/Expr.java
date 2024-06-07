package edu.sou.cs452.hw3j;

import java.util.List;

/**
 * The Expr class for Little Pony differs significantly from the Expr class used
 * in Lox, primarily due to the conceptual difference between expressions and
 * statements in the two languages. In Lox, a clear distinction is made between
 * expressions (which produce values) and statements (which perform actions).
 * This distinction is reflected in the design of the AST, where different
 * classes are used to represent expressions and statements.
 *
 * However, Little Pony treats what might traditionally be considered statements
 * as expressions. This means that constructs like actor declarations, method
 * calls, and constructor calls, which do not necessarily produce a value but
 * rather perform some action or define some structure, are represented as
 * expressions within the AST. This approach simplifies the AST structure for
 * Little Pony by using a unified hierarchy for both value-producing entities
 * and action-performing entities.
 *
 * As a result, in this homework assignment, Expr is used to represent almost
 * everything in Little Pony, blurring the lines between expressions and
 * statements. This design choice reflects Little Pony's emphasis on
 * expression-oriented programming, where the distinction between executing
 * actions (statements) and computing values (expressions) is minimized.
 */

abstract class Expr {
    interface Visitor<R> {
        R visitActorExpr(Actor expr);

        R visitMethodCallExpr(MethodCall expr);

        R visitLiteralExpr(Literal expr);

        R visitProgramExpr(Program expr);

        R visitMethodDeclarationExpr(MethodDeclaration expr);

        R visitVariableExpr(Variable expr);

        R visitMemberAccessExpr(MemberAccess expr);
    }

    static class Program extends Expr {
        final List<Expr> expressions;

        Program(List<Expr> expressions) {
            this.expressions = expressions;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitProgramExpr(this);
        }
    }

    static class Literal extends Expr {
        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    static class Actor extends Expr {
        final Token name;
        final List<Expr> body;

        Actor(Token name, List<Expr> body) {
            this.name = name;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitActorExpr(this);
        }
    }

    static class MethodCall extends Expr {
        final Token name;
        final List<Expr> arguments;
        final Expr obj;

        MethodCall(Expr obj, Token name, List<Expr> arguments) {
            this.obj = obj;
            this.name = name;
            this.arguments = arguments;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitMethodCallExpr(this);
        }
    }

    static class MethodDeclaration extends Expr {
        final Token name;
        final List<Token> params;
        final List<Expr> body;

        MethodDeclaration(Token name, List<Token> params, List<Expr> body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitMethodDeclarationExpr(this);
        }
    }

    static class MemberAccess extends Expr {
        final Expr obj;
        final Token name;

        MemberAccess(Expr obj, Token name) {
            this.obj = obj;
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitMemberAccessExpr(this);
        }
    }

    static class Variable extends Expr {
        final Token name;

        Variable(Token name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor); // Abstract method for accepting visitors.
}
