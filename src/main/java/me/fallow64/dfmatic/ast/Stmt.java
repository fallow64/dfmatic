package me.fallow64.dfmatic.ast;

import me.fallow64.dfmatic.Token;
import me.fallow64.dfmatic.TokenType;

import java.util.List;
import java.util.HashMap;

public abstract class Stmt {
    public interface Visitor<R> {
        R visitVariableStmt(Variable stmt);
        R visitReturnStmt(Return stmt);
        R visitBreakStmt(Break stmt);
        R visitContinueStmt(Continue stmt);
        R visitWhileStmt(While stmt);
        R visitLoopStmt(Loop stmt);
        R visitLoopForStmt(LoopFor stmt);
        R visitIfStmt(If stmt);
        R visitDFIfStmt(DFIf stmt);
        R visitDFStmt(DF stmt);
        R visitExpressionStmt(Expression stmt);
    }
    public static class Variable extends Stmt {
        public Variable(Token name, TokenType type, Expr expression) {
            this.name = name;
            this.type = type;
            this.expression = expression;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableStmt(this);
        }

        public final Token name;
        public final TokenType type;
        public final Expr expression;
    }
    public static class Return extends Stmt {
        public Return(Token keyword, Expr value) {
            this.keyword = keyword;
            this.value = value;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmt(this);
        }

        public final Token keyword;
        public final Expr value;
    }
    public static class Break extends Stmt {
        public Break(Token keyword) {
            this.keyword = keyword;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBreakStmt(this);
        }

        public final Token keyword;
    }
    public static class Continue extends Stmt {
        public Continue(Token keyword) {
            this.keyword = keyword;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitContinueStmt(this);
        }

        public final Token keyword;
    }
    public static class While extends Stmt {
        public While(Expr left, Token operator, Expr right, boolean inverted, List<Stmt> block) {
            this.left = left;
            this.operator = operator;
            this.right = right;
            this.inverted = inverted;
            this.block = block;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }

        public final Expr left;
        public final Token operator;
        public final Expr right;
        public final boolean inverted;
        public final List<Stmt> block;
    }
    public static class Loop extends Stmt {
        public Loop(Expr to, Expr from, Expr step, TokenType varType, Token varName, List<Stmt> block) {
            this.to = to;
            this.from = from;
            this.step = step;
            this.varType = varType;
            this.varName = varName;
            this.block = block;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLoopStmt(this);
        }

        public final Expr to;
        public final Expr from;
        public final Expr step;
        public final TokenType varType;
        public final Token varName;
        public final List<Stmt> block;
    }
    public static class LoopFor extends Stmt {
        public LoopFor(Token varName, TokenType varType, Expr list, List<Stmt> branch) {
            this.varName = varName;
            this.varType = varType;
            this.list = list;
            this.branch = branch;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLoopForStmt(this);
        }

        public final Token varName;
        public final TokenType varType;
        public final Expr list;
        public final List<Stmt> branch;
    }
    public static class If extends Stmt {
        public If(Expr left, Token operator, Expr right, boolean inverted, List<Stmt> ifBranch, List<Stmt> elseBranch) {
            this.left = left;
            this.operator = operator;
            this.right = right;
            this.inverted = inverted;
            this.ifBranch = ifBranch;
            this.elseBranch = elseBranch;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }

        public final Expr left;
        public final Token operator;
        public final Expr right;
        public final boolean inverted;
        public final List<Stmt> ifBranch;
        public final List<Stmt> elseBranch;
    }
    public static class DFIf extends Stmt {
        public DFIf(Token block, Token action, List<Expr> args, boolean inverted, List<Stmt> ifBranch, List<Stmt> elseBranch) {
            this.block = block;
            this.action = action;
            this.args = args;
            this.inverted = inverted;
            this.ifBranch = ifBranch;
            this.elseBranch = elseBranch;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitDFIfStmt(this);
        }

        public final Token block;
        public final Token action;
        public final List<Expr> args;
        public final boolean inverted;
        public final List<Stmt> ifBranch;
        public final List<Stmt> elseBranch;
    }
    public static class DF extends Stmt {
        public DF(Token blockName, Token actionName, HashMap<Token,Token> tags, List<Expr> arguments) {
            this.blockName = blockName;
            this.actionName = actionName;
            this.tags = tags;
            this.arguments = arguments;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitDFStmt(this);
        }

        public final Token blockName;
        public final Token actionName;
        public final HashMap<Token,Token> tags;
        public final List<Expr> arguments;
    }
    public static class Expression extends Stmt {
        public Expression(Expr expression) {
            this.expression = expression;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }

        public final Expr expression;
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
