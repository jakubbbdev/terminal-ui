package dev.jakub.terminal.internal;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;
import dev.jakub.terminal.interactive.Prompt;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Fluent builder for {@link Prompt}. Use via {@link Terminal#prompt(String)}.
 * Chain {@link #masked()} then {@link #ask()}.
 */
public final class PromptBuilder {

    private final String message;
    private final TerminalSupport support;
    private boolean masked;
    private PrintStream output = System.out;
    private InputStream input = System.in;

    public PromptBuilder(String message, TerminalSupport support) {
        this.message = message != null ? message : "";
        this.support = support != null ? support : new TerminalSupport();
    }

    /**
     * Hides input (e.g. for passwords).
     */
    public PromptBuilder masked() {
        this.masked = true;
        return this;
    }

    /**
     * Sets output stream (default: stdout).
     */
    public PromptBuilder output(PrintStream out) {
        this.output = out != null ? out : System.out;
        return this;
    }

    /**
     * Sets input stream (default: stdin). For tests.
     */
    public PromptBuilder input(InputStream in) {
        this.input = in != null ? in : System.in;
        return this;
    }

    /**
     * Prompts and returns the entered string.
     */
    public String ask() {
        Prompt p = new Prompt(message, support);
        if (masked) p.masked();
        p.output(output);
        p.input(input);
        return p.ask();
    }

    /**
     * Prompts and returns the next line from the given scanner. Use one shared
     * scanner so input blocks until the user types (avoids EOF when run from IDE).
     */
    public String ask(Scanner sharedScanner) {
        if (sharedScanner == null) return ask();
        Prompt p = new Prompt(message, support);
        if (masked) p.masked();
        p.output(output);
        p.input(input);
        return p.ask(sharedScanner);
    }
}
