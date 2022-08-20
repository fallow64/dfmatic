package me.fallow64.dfmatic;

import me.fallow64.builder.Builder;
import me.fallow64.builder.CodeTemplate;
import me.fallow64.dfmatic.ast.Sect;
import me.fallow64.util.ItemAPIUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
        if(templates != null) sendTemplates(templates);

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
    }

    private static void runRepl() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("DFMatic REPL v" + version);

        while(true) { // TODO make sure >>> is always the last line if possible
            System.out.print(">>> ");
            String source = scanner.next();
            if(Objects.equals(source, "")) continue;
            source = "event \"Join\" {" + source + "}";

            List<CodeTemplate> templates = run(source);
            if(templates != null) sendTemplates(templates);
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
        System.err.println("[line " + line + ":" + column + "] Error" + where + ": " + message);
        hadError = true;
    }

    private static void sendTemplates(List<CodeTemplate> templates) {
        boolean hadError = false;
        for(CodeTemplate template : templates) {
            try {
                hadError = hadError || !ItemAPIUtil.sendTemplate(template);
            } catch (Exception ignored) {}
        }
        if(hadError) {
            System.out.println("could not connect to code utils. give commands (use /dfgive clipboard):");
            for (CodeTemplate template : templates) {
                try {
                    System.out.println(template.genGiveCommand("DFMatic", 1));
                } catch (Exception ignored) {} // wow you really really fucked up
            }
        }
    }

}