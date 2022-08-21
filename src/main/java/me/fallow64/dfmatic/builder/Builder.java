package me.fallow64.dfmatic.builder;

import me.fallow64.dfmatic.Token;
import me.fallow64.dfmatic.TokenType;
import me.fallow64.dfmatic.ast.Expr;
import me.fallow64.dfmatic.ast.Sect;
import me.fallow64.dfmatic.ast.Stmt;
import me.fallow64.dfmatic.builder.blocks.BracketDirection;
import me.fallow64.dfmatic.builder.blocks.CodeBlock;
import me.fallow64.dfmatic.builder.blocks.CustomBlock;
import me.fallow64.dfmatic.builder.blocks.impl.*;
import me.fallow64.dfmatic.builder.values.CodeValue;
import me.fallow64.dfmatic.builder.values.VariableScope;
import me.fallow64.dfmatic.builder.values.impl.Number;
import me.fallow64.dfmatic.builder.values.impl.*;
import me.fallow64.dfmatic.util.RandomUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Builder implements Sect.Visitor<CodeTemplate>, Stmt.Visitor<Void>, Expr.Visitor<CodeValue> {

    private final List<Sect> sections;
    private final List<CodeTemplate> templates;
    private List<CodeBlock> currentStack;
    private final HashMap<String, VariableScope> environment;

    public Builder(List<Sect> sections) {
        this.sections = sections;
        this.templates = new ArrayList<>();
        this.currentStack = new ArrayList<>();
        this.environment = new HashMap<>();
    }

    public List<CodeTemplate> build() {
        for (Sect section : sections) {
            templates.add(resolve(section));
            currentStack = new ArrayList<>(); // not efficient
        }
        return templates;
    }

    @Override
    public CodeTemplate visitEventSect(Sect.Event sect) {
        currentStack.add(new Event((String) sect.name.getLiteral()));
        resolve(sect.block);
        return new CodeTemplate(currentStack);
    }

    @Override
    public CodeTemplate visitFunctionSect(Sect.Function sect) {
        currentStack.add(new Function(sect.name.getLexeme(), List.of(new Tag("False", "Is Hidden", "dynamic", "func"))));

        for(int i = 1; i < sect.parameters.size() + 1; i++) {
            Token parameter = sect.parameters.get(i - 1);
            String name = parameter.getLexeme();
            environment.put(name, VariableScope.LOCAL);
            currentStack.add(new SetVariable("GetListValue", List.of(), List.of(
                    new Variable(name, VariableScope.LOCAL),
                    new Variable("$ia", VariableScope.LOCAL),
                    new Number(Integer.toString(i))
            )));
        }

        resolve(sect.block);
        return new CodeTemplate(currentStack);
    }

    @Override
    public Void visitVariableStmt(Stmt.Variable stmt) {
        String varName = stmt.name.getLexeme();
        VariableScope variableScope = toScope(stmt.type);

        environment.put(varName, variableScope);

        if(stmt.expression != null) {
            currentStack.add(new SetVariable("=", List.of(), List.of(
                    new Variable(varName, variableScope),
                    resolve(stmt.expression)
            )));
        }
        return null;
    }

    @Override
    public Void visitLoopForStmt(Stmt.LoopFor stmt) {
        VariableScope scope = toScope(stmt.varType);
        String varName = stmt.varName.getLexeme();

        Variable iterator = new Variable(varName, scope);
        currentStack.add(new Repeat("ForEach", List.of(new Tag("True", "Allow List Changes", "ForEach", "repeat")), List.of(iterator, resolve(stmt.list))));
        currentStack.add(new RepeatBracket(BracketDirection.OPEN));
        resolve(stmt.branch);
        currentStack.add(new RepeatBracket(BracketDirection.CLOSE));
        return null;
    }

    @Override
    public Void visitLoopStmt(Stmt.Loop stmt) {
        if(stmt.varName != null) {
            List<CodeValue> arguments = new ArrayList<>();
            arguments.add(new Variable(stmt.varName.getLexeme(), toScope(stmt.varType))); //iterator variable
            arguments.add(resolve(stmt.from));
            arguments.add(resolve(stmt.to));
            arguments.add(resolve(stmt.step));
            environment.put(stmt.varName.getLexeme(), toScope(stmt.varType));

            currentStack.add(new Repeat("Range", List.of(), arguments));
        } else if(stmt.to != null) {
            currentStack.add(new Repeat("Multiple", List.of(), List.of(resolve(stmt.to))));
        } else {
            currentStack.add(new Repeat("Forever", List.of(), List.of()));
        }
        currentStack.add(new RepeatBracket(BracketDirection.OPEN));
        resolve(stmt.block);
        currentStack.add(new RepeatBracket(BracketDirection.CLOSE));
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        currentStack.add(new SetVariable("=", List.of(), List.of(
                new Variable("$rv", VariableScope.LOCAL),
                stmt.value != null ? resolve(stmt.value) : new Number("0")
        )));
        currentStack.add(new Control("Return", List.of(), List.of()));
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {

        String subaction = Objects.equals(stmt.operator.getLexeme(), "==") ? "=" : stmt.operator.getLexeme();

        currentStack.add(new Repeat(
            "While",
            subaction,
            stmt.inverted,
            List.of(),
            List.of(resolve(stmt.left), resolve(stmt.right))
        ));

        currentStack.add(new RepeatBracket(BracketDirection.OPEN));
        resolve(stmt.block);
        currentStack.add(new RepeatBracket(BracketDirection.CLOSE));
        return null;
    }

    @Override
    public Void visitBreakStmt(Stmt.Break stmt) {
        currentStack.add(new Control("StopRepeat", List.of(), List.of()));
        return null;
    }

    @Override
    public Void visitContinueStmt(Stmt.Continue stmt) {
        currentStack.add(new Control("Skip", List.of(), List.of()));
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        String action = Objects.equals(stmt.operator.getLexeme(), "==") ? "=" : stmt.operator.getLexeme();

        CodeValue left = resolve(stmt.left);
        CodeValue right = resolve(stmt.right);

        currentStack.add(new IfVariable(action, stmt.inverted, List.of(), List.of(left, right)));
        currentStack.add(new Bracket(BracketDirection.OPEN));
        resolve(stmt.ifBranch);
        currentStack.add(new Bracket(BracketDirection.CLOSE));
        if(stmt.elseBranch != null) {
            currentStack.add(new Else());
            currentStack.add(new Bracket(BracketDirection.OPEN));
            resolve(stmt.elseBranch);
            currentStack.add(new Bracket(BracketDirection.CLOSE));
        }
        return null;
    }

    @Override
    public Void visitDFIfStmt(Stmt.DFIf stmt) {
        String blockName = (String)stmt.block.getLiteral();
        String actionName = (String)stmt.action.getLiteral();
        currentStack.add(new CustomBlock(blockName, actionName, List.of(), resolveExprs(stmt.args)));
        currentStack.add(new Bracket(BracketDirection.OPEN));
        resolve(stmt.ifBranch);
        currentStack.add(new Bracket(BracketDirection.CLOSE));
        if(stmt.elseBranch != null) {
            currentStack.add(new Else());
            currentStack.add(new Bracket(BracketDirection.OPEN));
            resolve(stmt.elseBranch);
            currentStack.add(new Bracket(BracketDirection.CLOSE));
        }
        return null;
    }

    @Override
    public Void visitDFStmt(Stmt.DF stmt) {
        String blockName = (String)stmt.blockName.getLiteral();
        String actionName = (String)stmt.actionName.getLiteral();
        currentStack.add(new CustomBlock(blockName, actionName,
                Tag.fromHashmapFromToken(stmt.tags, actionName, blockName), resolveExprs(stmt.arguments)));
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public CodeValue visitAssignExpr(Expr.Assign expr) {
        String varName = expr.name.getLexeme();
        VariableScope scope = getScope(varName);
        currentStack.add(new SetVariable("=", List.of(), List.of(
                new Variable(varName, scope),
                resolve(expr.value)
        )));
        return new Variable(varName, scope);
    }

    @Override
    public CodeValue visitGetExpr(Expr.Get expr) {
        CodeValue left = resolve(expr.left);
        CodeValue randomVar = RandomUtil.randomVar();
        Text property = new Text(expr.name.getTokenType() == TokenType.STRING ? (String)expr.name.getLiteral() : expr.name.getLexeme());
        currentStack.add(new SetVariable("GetDictValue", List.of(), List.of(
                randomVar,
                left,
                property
        )));
        return randomVar;
    }

    @Override
    public CodeValue visitIndexExpr(Expr.Index expr) {
        CodeValue left = resolve(expr.left);
        CodeValue right = resolve(expr.index);
        Variable randomVar = RandomUtil.randomVar();
        if(right instanceof Text) { // this version of get dict value doesn't support expressions, so that's the use of Stmt.Get
            currentStack.add(new SetVariable("GetDictValue", List.of(), List.of(
                    randomVar,
                    left,
                    right
            )));
        } else {
            currentStack.add(new SetVariable("GetListValue", List.of(), List.of(
                    randomVar,
                    left,
                    right
            )));
        }
        return randomVar;
    }

    @Override
    public CodeValue visitCallExpr(Expr.Call expr) {
        List<CodeValue> values = resolveExprs(expr.arguments);
        List<CodeValue> arguments = new ArrayList<>();
        arguments.add(new Variable("$ia", VariableScope.LOCAL));
        arguments.addAll(values);

        currentStack.add(new SetVariable("CreateList", List.of(), arguments));
        currentStack.add(new CallFunction(expr.name.getLexeme()));
        return new Variable("$rv", VariableScope.LOCAL);
    }

    @Override
    public CodeValue visitGroupingExpr(Expr.Grouping expr) {
        return resolve(expr.expression);
    }

    @Override
    public CodeValue visitLiteralExpr(Expr.Literal expr) {
        if(expr.value instanceof Double) {
            return new Number(expr.value.toString());
        } else if(expr.value instanceof String) {
            return Text.fromColorCode((String) expr.value);
        } else if(expr.value instanceof List<?>) {
            List<Expr> exprs = (List<Expr>)expr.value; // unreachable error TODO clean this and dictionary
            CodeValue tempVar = RandomUtil.randomVar();
            ArrayList<CodeValue> values = new ArrayList<>(List.of(tempVar));
            values.addAll(resolveExprs(exprs));
            currentStack.add(new SetVariable("CreateList", List.of(), values));
            return tempVar;
        } else if(expr.value instanceof HashMap<?,?>) {
            HashMap<Token, Expr> hashMap = (HashMap<Token, Expr>) expr.value; // unreachable error
            Variable list1 = RandomUtil.randomVar();
            Variable list2 = RandomUtil.randomVar();
            List<CodeValue> keys = new ArrayList<>();
            keys.add(list1);
            List<CodeValue> values = new ArrayList<>();
            values.add(list2);
            Variable result = RandomUtil.randomVar();
            hashMap.forEach((k, v) -> {
                keys.add(new Text((String)k.getLiteral()));
                values.add(resolve(v));
            });
            currentStack.add(new SetVariable("CreateList", List.of(), keys));
            currentStack.add(new SetVariable("CreateList", List.of(), values));
            currentStack.add(new SetVariable("CreateDict", List.of(), List.of(result, list1, list2)));
            return result;
        } else if(expr.value instanceof GameValue) {
            return (CodeValue) expr.value;
        }
        return null; //unreachable
    }

    @Override
    public CodeValue visitBinaryExpr(Expr.Binary expr) {
        CodeValue left = resolve(expr.left);
        String leftString = left instanceof Number ? ((Number) left).getValue() : "%var(" + ((Variable) left).getName() + ")";
        CodeValue right = resolve(expr.right);
        String rightString = right instanceof Number ? ((Number) right).getValue() : "%var(" + ((Variable) right).getName() + ")";
        String result = "%math(" + leftString + " " + expr.operator.getLexeme() + " " + rightString + ")";
        return new Number(result);
    }

    @Override
    public CodeValue visitUnaryExpr(Expr.Unary expr) {
        CodeValue value = resolve(expr.right);
        String valueString = value instanceof Number ? ((Number) value).getValue() : "%var(" + ((Variable) value).getName() + ")";
        return new Number("%math(" + valueString + "*-1)");
    }

    @Override
    public CodeValue visitVariableExpr(Expr.Variable expr) {
        VariableScope scope = getScope(expr.name.getLexeme());
        return new Variable(expr.name.getLexeme(), scope);
    }

    public VariableScope getScope(String varName) {
        VariableScope result = environment.get(varName);
        if(result == null) {
            environment.put(varName, VariableScope.LOCAL);
            result = VariableScope.LOCAL;
        }
        return result;
    }

    public void resolve(List<Stmt> block) {
        for(Stmt stmt : block) {
            resolve(stmt);
        }
    }

    public CodeTemplate resolve(Sect section) {
        return section.accept(this);
    }

    public void resolve(Stmt statement) {
        statement.accept(this);
    }

    public List<CodeValue> resolveExprs(List<Expr> expressions) {
        List<CodeValue> result = new ArrayList<>();
        for(Expr expr : expressions) {
            result.add(resolve(expr));
        }
        return result;
    }

    public CodeValue resolve(Expr expr) {
        return expr.accept(this);
    }

    public VariableScope toScope(TokenType type) {
        switch(type) {
            case SAVE -> { return VariableScope.SAVE; }
            case GAME -> { return VariableScope.GAME; }
            case LOCAL -> { return VariableScope.LOCAL; }
            default -> { return null; }
        }
    }
}
