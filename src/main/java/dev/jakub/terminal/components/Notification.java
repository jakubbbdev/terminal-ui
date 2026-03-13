package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;

/**
 * Single notification line. Use via {@link Terminal#notify(String, Notification.Type)}.
 * Each type has icon + color. Prints immediately (no stack).
 */
public final class Notification {

    private final String message;
    private final Type type;
    private final TerminalSupport support;

    public Notification(String message, Type type, TerminalSupport support) {
        this.message = message != null ? message : "";
        this.type = type != null ? type : Type.INFO;
        this.support = support;
    }

    /**
     * Prints the notification to the given stream (with newline).
     */
    public void print(PrintStream out) {
        String icon = icon(this.type);
        if (support.isAnsiEnabled()) {
            out.print(color(type));
        }
        out.print(icon + "  " + message);
        if (support.isAnsiEnabled()) {
            out.print(Ansi.RESET);
        }
        out.println();
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }

    private String icon(Type t) {
        boolean ascii = !support.isUtf8Symbols();
        if (!ascii) {
            return switch (t) {
                case SUCCESS -> "✅";
                case WARNING -> "⚠️";
                case ERROR -> "❌";
                case INFO -> "ℹ️";
            };
        }
        return switch (t) {
            case SUCCESS -> "[OK]";
            case WARNING -> "[!]";
            case ERROR -> "[X]";
            case INFO -> "[i]";
        };
    }

    private static String color(Type t) {
        return switch (t) {
            case SUCCESS -> Ansi.FG_GREEN;
            case WARNING -> Ansi.FG_YELLOW;
            case ERROR -> Ansi.FG_RED;
            case INFO -> Ansi.FG_CYAN;
        };
    }

    /**
     * Notification type.
     */
    public enum Type {
        SUCCESS, WARNING, ERROR, INFO
    }
}
