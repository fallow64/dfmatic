package me.fallow64.dfmatic.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generateAst <output directory>");
            System.exit(1);
        }
        String outputDir = args[0];

        defineAst(outputDir, "Expr", Arrays.asList(
                "Assign     : Token name | Expr value",
                "Call       : Token name | List<Expr> arguments",
                "Get        : Expr left | Token name",
                "Index      : Expr left | Expr index",
                "Grouping   : Expr expression",
                "Literal    : Object value",
                "Binary     : Expr left | Token operator | Expr right",
                "Unary      : Token operator | Expr right",
                "Variable   : Token name"
        ));

        defineAst(outputDir, "Sect", Arrays.asList(
                "Event      : Token name | List<Stmt> block",
                "Function   : Token name | List<Token> parameters | List<Stmt> block"
        ));

        defineAst(outputDir, "Stmt", Arrays.asList(
                "Variable   : Token name | TokenType type | Expr expression",
                "Return     : Token keyword | Expr value",
                "Break      : Token keyword",
                "Continue   : Token keyword",
                "While      : Expr left | Token operator | Expr right | boolean inverted | List<Stmt> block",
                "Loop       : Expr to | Expr from | Expr step | TokenType varType | Token varName | List<Stmt> block", // shamelessly stolen syntax from spark
                "LoopFor    : Token varName | TokenType varType | Expr list | List<Stmt> branch",
                "If         : Expr left | Token operator | Expr right | boolean inverted | List<Stmt> ifBranch | List<Stmt> elseBranch",
                "DFIf       : Token block | Token action | List<Expr> args | boolean inverted | List<Stmt> ifBranch | List<Stmt> elseBranch",
                "DF         : Token blockName | Token actionName | HashMap<Token,Token> tags | List<Expr> arguments",
                "Expression : Expr expression"
        ));

    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        writer.println("package me.fallow64.dfmatic.ast;");
        writer.println("");
        writer.println("import me.fallow64.dfmatic.Token;");
        writer.println("import me.fallow64.dfmatic.TokenType;");
        writer.println("");
        writer.println("import java.util.List;");
        writer.println("import java.util.HashMap;");
        writer.println("");
        writer.println("public abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        // The AST classes.
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        // The base accept() method.
        writer.println("");
        writer.println("    public abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    public interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("        R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("    public static class " + className + " extends " + baseName + " {");

        // Constructor.
        writer.println("        public " + className + "(" + String.join(", ", fieldList.split(" \\| ")) + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(" \\| ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }

        writer.println("        }");

        // Visitor pattern.
        writer.println();
        writer.println("        public <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseName + "(this);");
        writer.println("        }");

        // Fields.
        writer.println();
        for (String field : fields) {
            writer.println("        public final " + field + ";");
        }

        writer.println("    }");
    }
}