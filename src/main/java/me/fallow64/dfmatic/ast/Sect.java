package me.fallow64.dfmatic.ast;

import me.fallow64.dfmatic.Token;
import me.fallow64.dfmatic.TokenType;

import java.util.List;
import java.util.HashMap;

public abstract class Sect {
    public interface Visitor<R> {
        R visitEventSect(Event sect);
        R visitFunctionSect(Function sect);
    }
    public static class Event extends Sect {
        public Event(Token name, List<Stmt> block) {
            this.name = name;
            this.block = block;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitEventSect(this);
        }

        public final Token name;
        public final List<Stmt> block;
    }
    public static class Function extends Sect {
        public Function(Token name, List<Token> parameters, List<Stmt> block) {
            this.name = name;
            this.parameters = parameters;
            this.block = block;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionSect(this);
        }

        public final Token name;
        public final List<Token> parameters;
        public final List<Stmt> block;
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
