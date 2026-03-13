package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;

/**
 * Lightweight Markdown renderer for terminal. Use via {@link Terminal#markdown(String)}.
 * Supports # headings, **bold**, *italic*, `code`, --- rules, - list.
 */
public final class Markdown {

    private final TerminalSupport support;
    private final String source;

    public Markdown(String source, TerminalSupport support) {
        this.source = source != null ? source : "";
        this.support = support;
    }

    /**
     * Renders and prints to the given stream.
     */
    public void print(PrintStream out) {
        String[] lines = source.split("\\n", -1);
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("#")) {
                int level = 0;
                while (level < line.length() && line.charAt(level) == '#') level++;
                String rest = line.substring(level).trim();
                if (support.isAnsiEnabled()) out.print(Ansi.BOLD);
                out.println(rest);
                if (support.isAnsiEnabled()) out.print(Ansi.RESET);
            } else if (line.equals("---") || line.startsWith("---")) {
                String rule = support.isUtf8Symbols() ? "─" : "-";
                out.println(rule.repeat(Math.min(40, support.getWidth())));
            } else if (line.startsWith("- ") || line.startsWith("* ")) {
                String bullet = support.isUtf8Symbols() ? "  • " : "  - ";
                out.println(bullet + renderInline(line.substring(2)));
            } else {
                out.println(renderInline(line));
            }
        }
    }

    private String renderInline(String s) {
        if (!support.isAnsiEnabled()) {
            return s.replaceAll("\\*\\*(.+?)\\*\\*", "$1").replaceAll("\\*(.+?)\\*", "$1").replaceAll("`(.+?)`", "$1");
        }
        s = s.replaceAll("\\*\\*(.+?)\\*\\*", Ansi.BOLD + "$1" + Ansi.RESET);
        s = s.replaceAll("\\*(.+?)\\*", Ansi.ITALIC + "$1" + Ansi.RESET);
        s = s.replaceAll("`(.+?)`", Ansi.FG_CYAN + "$1" + Ansi.RESET);
        return s;
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }
}
