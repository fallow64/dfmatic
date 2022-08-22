package me.fallow64.dfmatic.ast;

import me.fallow64.dfmatic.Token;

import java.util.List;

public abstract class Expr {
    public interface Visitor<R> {
        R visitAssignExpr(Assign expr);
        R visitCallExpr(Call expr);
        R visitGetExpr(Get expr);
        R visitIndexExpr(Index expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
        R visitBinaryExpr(Binary expr);
        R visitUnaryExpr(Unary expr);
        R visitVariableExpr(Variable expr);
    }
    public static class Assign extends Expr {
        public Assign(Expr left, Expr value) {
            this.left = left;
            this.value = value;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }

        public final Expr left;
        public final Expr value;
    }
    public static class Call extends Expr {
        public Call(Token name, List<Expr> arguments) {
            this.name = name;
            this.arguments = arguments;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

        public final Token name;
        public final List<Expr> arguments;
    }
    public static class Get extends Expr {
        public Get(Expr left, Token name) {
            this.left = left;
            this.name = name;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }

        public final Expr left;
        public final Token name;
    }
    public static class Index extends Expr {
        public Index(Expr left, Expr index) {
            this.left = left;
            this.index = index;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIndexExpr(this);
        }

        public final Expr left;
        public final Expr index;
    }
    public static class Grouping extends Expr {
        public Grouping(Expr expression) {
            this.expression = expression;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        public final Expr expression;
    }
    public static class Literal extends Expr {
        public Literal(Object value) {
            this.value = value;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        public final Object value;
    }
    public static class Binary extends Expr {
        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        public final Expr left;
        public final Token operator;
        public final Expr right;
    }
    public static class Unary extends Expr {
        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        public final Token operator;
        public final Expr right;
    }
    public static class Variable extends Expr {
        public Variable(Token name) {
            this.name = name;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

        public final Token name;
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
