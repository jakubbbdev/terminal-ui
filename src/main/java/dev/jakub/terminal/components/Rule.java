package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;

/**
 * A horizontal rule/separator line. Use via {@link Terminal#rule()}.
 */
public final class Rule {

    private final TerminalSupport support;
    private final int width;
    private char character = '-';
    private String prefix = "";
    private String suffix = "";

    public Rule(TerminalSupport support) {
        this.support = support;
        this.width = Math.min(support.getWidth(), 120);
    }

    /**
     * Uses the given character for the line (e.g. '-', '=', '*').
     */
    public Rule character(char c) {
        this.character = c;
        return this;
    }

    /**
     * Uses '=' for the line.
     */
    public Rule doubles() {
        return character('=');
    }

    /**
     * Optional text before the line.
     */
    public Rule prefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
        return this;
    }

    /**
     * Optional text after the line.
     */
    public Rule suffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
        return this;
    }

    /**
     * Prints the rule to the given stream.
     */
    public void print(PrintStream out) {
        int lineLen = Math.max(0, width - prefix.length() - suffix.length());
        String line = prefix + String.valueOf(character).repeat(Math.max(0, lineLen)) + suffix;
        out.println(line);
    }

    /**
     * Prints the rule to stdout.
     */
    public void print() {
        print(System.out);
    }
}
