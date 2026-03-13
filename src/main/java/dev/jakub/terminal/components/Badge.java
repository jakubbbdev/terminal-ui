package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.Color;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;

/**
 * Colored bracket label (notification badge). Use via {@link Terminal#badge(String, Color)}.
 * Chainable like StyledText: .println() / .print().
 */
public final class Badge {

    private final String label;
    private final Color color;
    private final TerminalSupport support;

    public Badge(String label, Color color, TerminalSupport support) {
        this.label = label != null ? label : "";
        this.color = color != null ? color : Color.WHITE;
        this.support = support;
    }

    /**
     * Prints the badge to the given stream (no newline).
     */
    public void print(PrintStream out) {
        String text = "[" + label + "]";
        if (support.isAnsiEnabled()) {
            out.print(color.ansiCode());
            out.print(text);
            out.print(Ansi.RESET);
        } else {
            out.print(text);
        }
    }

    /**
     * Prints the badge to stdout (no newline).
     */
    public void print() {
        print(System.out);
    }

    /**
     * Prints the badge followed by a newline.
     */
    public void println(PrintStream out) {
        print(out);
        out.println();
    }

    /**
     * Prints the badge to stdout followed by a newline.
     */
    public void println() {
        println(System.out);
    }
}
