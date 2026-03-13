package dev.jakub.terminal.interactive;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Yes/No confirm dialog. Use via {@link Terminal#confirm(String)}.
 * [Y/n] or [y/N] based on default. Injectable in/out for tests.
 */
public final class Confirm {

    private final String message;
    private final TerminalSupport support;
    private boolean defaultYes = true;
    private PrintStream out = System.out;
    private InputStream in = System.in;

    public Confirm(String message, TerminalSupport support) {
        this.message = message != null ? message : "";
        this.support = support;
    }

    /**
     * Sets default to No ([y/N]).
     */
    public Confirm defaultNo() {
        this.defaultYes = false;
        return this;
    }

    /**
     * Sets default to Yes ([Y/n]). This is the default.
     */
    public Confirm defaultYes() {
        this.defaultYes = true;
        return this;
    }

    /**
     * Sets output stream.
     */
    public Confirm output(PrintStream out) {
        this.out = out != null ? out : System.out;
        return this;
    }

    /**
     * Sets input stream (for tests).
     */
    public Confirm input(InputStream in) {
        this.in = in != null ? in : System.in;
        return this;
    }

    /**
     * Prompts and returns true for yes, false for no.
     */
    public boolean ask() {
        String prompt = defaultYes ? " [Y/n]: " : " [y/N]: ";
        out.print(message + prompt);
        out.flush();
        try (Scanner scan = new Scanner(in)) {
            if (!scan.hasNextLine()) return defaultYes;
            String line = scan.nextLine().trim().toLowerCase();
            if (line.isEmpty()) return defaultYes;
            return "y".equals(line) || "yes".equals(line);
        }
    }

    /**
     * Uses the given scanner for one line. Use when sharing one scanner (e.g. interactive app).
     */
    public boolean ask(Scanner sharedScanner) {
        if (sharedScanner == null) return ask();
        String prompt = defaultYes ? " [Y/n]: " : " [y/N]: ";
        out.print(message + prompt);
        out.flush();
        if (!sharedScanner.hasNextLine()) return defaultYes;
        String line = sharedScanner.nextLine().trim().toLowerCase();
        if (line.isEmpty()) return defaultYes;
        return "y".equals(line) || "yes".equals(line);
    }
}
