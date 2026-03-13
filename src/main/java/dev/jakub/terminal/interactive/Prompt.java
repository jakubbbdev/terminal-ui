package dev.jakub.terminal.interactive;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Input prompt for reading user input. Use via {@link Terminal#prompt(String)}.
 * Supports masked input for passwords. Injectable input/output for tests.
 */
public final class Prompt {

    private final String message;
    private final TerminalSupport support;
    private boolean masked;
    private PrintStream out = System.out;
    private InputStream in = System.in;

    public Prompt(String message, TerminalSupport support) {
        this.message = message != null ? message : "";
        this.support = support;
    }

    /**
     * Hides input (e.g. for passwords). Shows * per character or blank.
     */
    public Prompt masked() {
        this.masked = true;
        return this;
    }

    /**
     * Sets the output stream (default: stdout).
     */
    public Prompt output(PrintStream out) {
        this.out = out != null ? out : System.out;
        return this;
    }

    /**
     * Sets the input stream (default: stdin). Useful for tests.
     */
    public Prompt input(InputStream in) {
        this.in = in != null ? in : System.in;
        return this;
    }

    /**
     * Prompts and returns the entered string. Blocks until a line is read.
     */
    public String ask() {
        out.print(message);
        out.flush();
        if (masked) {
            return readMasked();
        }
        try (Scanner scan = new Scanner(in)) {
            return scan.hasNextLine() ? scan.nextLine() : "";
        }
    }

    /**
     * Prompts and returns the next line from the given scanner. Use this when
     * sharing one scanner (e.g. in an interactive app) so input blocks correctly.
     */
    public String ask(Scanner sharedScanner) {
        if (sharedScanner == null) return ask();
        out.print(message);
        out.flush();
        if (masked && System.console() != null) {
            char[] chars = System.console().readPassword();
            return chars != null ? new String(chars) : "";
        }
        return sharedScanner.hasNextLine() ? sharedScanner.nextLine() : "";
    }

    private String readMasked() {
        if (System.console() != null) {
            char[] chars = System.console().readPassword();
            return chars != null ? new String(chars) : "";
        }
        try (Scanner scan = new Scanner(in)) {
            return scan.hasNextLine() ? scan.nextLine() : "";
        }
    }
}
