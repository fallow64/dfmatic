package me.fallow64.dfmatic;

import me.fallow64.builder.Builder;
import me.fallow64.builder.CodeTemplate;
import me.fallow64.dfmatic.ast.Sect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class DFMatic {

    public static boolean hadError = false;
    public static final String version = "0.1";

    public static void main(String[] args) throws IOException {
        if(args.length == 1) {
            runFile(args[0]);
        } else {
            runRepl();
        }
    }

    public static void run(String source) throws IOException {
        List<Token> tokens = new Lexer(source).scanTokens();
        if(hadError) return;
        List<Sect> sections = new Parser(tokens).parse();
        if(hadError) return;
        List<CodeTemplate> codeTemplates = new Builder(sections).build();
        for (CodeTemplate codeTemplate : codeTemplates) {
            codeTemplate.sendTemplate();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
    }

    private static void runRepl() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        System.out.println("DFMatic REPL v" + version);

        while(true) {
            System.out.print(">>> ");
            String source = reader.readLine();
            if(Objects.equals(source, "")) continue;
            source = "event \"Join\" {" + source + "}";
            run(source);
            hadError = false;
        }
    }

    public static void error(int line, String message) {
        report(line, "", message);
    }

    public static void error(Token token, String message) {
        if(token.getTokenType() != TokenType.EOF) {
            report(token.getLineNumber(), " at '" + token.getLexeme() + "'", message);
        } else {
            report(token.getLineNumber(), " at end", message);
        }
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

}