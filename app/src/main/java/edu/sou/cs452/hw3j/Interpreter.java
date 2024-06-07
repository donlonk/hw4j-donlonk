package edu.sou.cs452.hw3j;

import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case PLUS:
                // Assuming operands are numbers or strings
                if (left instanceof Double && right instanceof Double) {
                    return (Double) left + (Double) right;
                }
                if (left instanceof String || right instanceof String) {
                    return String.valueOf(left) + String.valueOf(right);
                }
                throw new RuntimeException("Operands must be two numbers or two strings.");
            case GREATER:
                // Assuming operands are numbers
                if (left instanceof Double && right instanceof Double) {
                    return (Double) left > (Double) right;
                }
                throw new RuntimeException("Operands must be two numbers.");
            // Add other cases for different operators if needed
            default:
                throw new RuntimeException("Unknown operator: " + expr.operator.type);
        }
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case NEW:
                // Simplified handling of new
                return "new " + right.toString();
            case ENV:
                // Simplified handling of env
                return "env " + right.toString();
            // Add other cases for different operators if needed
            default:
                throw new RuntimeException("Unknown operator: " + expr.operator.type);
        }
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        // Simplified variable handling, assume variables have been defined
        return "variable " + expr.name.lexeme;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (Boolean) object;
        return true;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        // Store the variable in the environment (not implemented in this example)
        return null;
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeException error) {
            System.err.println("Runtime error: " + error.getMessage());
        }
    }

    private Void execute(Stmt stmt) {
        return stmt.accept(this);
    }
}
