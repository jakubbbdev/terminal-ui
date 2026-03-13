package dev.jakub.terminal.core;

import dev.jakub.terminal.Terminal;

import java.io.PrintStream;

/**
 * Fluent builder for colored and styled terminal output.
 * Use via {@link Terminal#print(String)}.
 */
public final class StyledText {

    private final String text;
    private final TerminalSupport support;
    private Color color;
    private boolean bold;
    private boolean dim;
    private boolean underline;

    public StyledText(String text, TerminalSupport support) {
        this.text = text != null ? text : "";
        this.support = support;
    }

    /**
     * Applies the given foreground color.
     */
    public StyledText color(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Applies bold styling.
     */
    public StyledText bold() {
        this.bold = true;
        return this;
    }

    /**
     * Applies dim styling.
     */
    public StyledText dim() {
        this.dim = true;
        return this;
    }

    /**
     * Applies underline styling.
     */
    public StyledText underline() {
        this.underline = true;
        return this;
    }

    /**
     * Writes the styled text to the given stream without a newline.
     */
    public void print(PrintStream out) {
        if (support.isAnsiEnabled()) {
            StringBuilder sb = new StringBuilder();
            if (bold) sb.append(Ansi.BOLD);
            if (dim) sb.append(Ansi.DIM);
            if (underline) sb.append(Ansi.UNDERLINE);
            if (color != null) sb.append(color.ansiCode());
            sb.append(text);
            sb.append(Ansi.RESET);
            out.print(sb.toString());
        } else {
            out.print(text);
        }
    }

    /**
     * Writes the styled text to stdout without a newline.
     */
    public void print() {
        print(System.out);
    }

    /**
     * Writes the styled text to the given stream followed by a newline.
     */
    public void println(PrintStream out) {
        print(out);
        out.println();
    }

    /**
     * Writes the styled text to stdout followed by a newline.
     */
    public void println() {
        println(System.out);
    }
}
