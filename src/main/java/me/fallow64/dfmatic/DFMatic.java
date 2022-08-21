package me.fallow64.dfmatic;

import me.fallow64.builder.Builder;
import me.fallow64.builder.CodeTemplate;
import me.fallow64.dfmatic.ast.Sect;
import me.fallow64.util.ItemAPIUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class DFMatic {

    public static boolean hadError = false;
    public static boolean codeUtils = true;
    public static final String version = "0.1";

    public static void main(String[] args) throws IOException {
        if(args.length >= 1) {
            runFile(args[0]);
        } else {
            runRepl();
        }
    }

    public static List<CodeTemplate> run(String source) {
        List<Token> tokens = new Lexer(source).scanTokens();
        if(hadError) return null;
        List<Sect> sections = new Parser(tokens).parse();
        if(hadError) return null;
        return new Builder(sections).build();
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        List<CodeTemplate> templates = run(new String(bytes, Charset.defaultCharset()));
        if(templates != null) sendTemplates(templates, true);

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
    }

    private static void runRepl() {
        System.out.println("DFMatic REPL v" + version);
        System.out.println("Warning: The REPL is very glitchy. Use with caution.");

        while(true) { // TODO make sure >>> is always the last line if possible
            System.out.print(">>> ");
            Scanner scanner = new Scanner(System.in);
            String source = scanner.nextLine();
            source = "event \"Join\" {" + source + "}";

            List<CodeTemplate> templates = run(source);
            if(templates != null) sendTemplates(templates, false);
            hadError = false;
        }
    }

    public static void error(int line, int column, String message) {
        report(line, column, "", message);
    }

    public static void error(Token token, String message) {
        if(token.getTokenType() != TokenType.EOF) {
            report(token.getLineNumber(), token.getColumnNumber(), " at '" + token.getLexeme() + "'", message);
        } else {
            report(token.getLineNumber(), token.getColumnNumber(), " at end", message);
        }
    }

    private static void report(int line, int column, String where, String message) {
        // this does not use System.err because of buffers messing up the order and stuff
        System.out.println("[line " + line + ":" + column + "] Error" + where + ": " + message);
        hadError = true;
    }

    private static void sendTemplates(List<CodeTemplate> templates, boolean quitOnSend) {
        if(codeUtils) {
            System.out.println("attempting to send to item api...");
            ItemAPIUtil.sendTemplates(templates, quitOnSend);
        } else {
            System.out.println("/give commands:");
            for (CodeTemplate template : templates) {
                System.out.println(template.genGiveCommand("DFMatic", 1));
            }
            if(quitOnSend) System.exit(0);
        }
    }

}