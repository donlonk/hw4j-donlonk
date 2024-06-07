package edu.sou.cs452.hw3j;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.List;

public class App {
    private final Interpreter interpreter = new Interpreter();

    public void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();
        System.out.println("Statements: " + statements); // Add debug print

        interpreter.interpret(statements);
    }

    public void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            new App().runFile(args[0]);
        } else {
            // Implement prompt mode if needed
        }
    }
}
