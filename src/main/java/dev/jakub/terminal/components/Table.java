package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for ASCII tables. Use via {@link Terminal#table()}.
 */
public final class Table {

    private final TerminalSupport support;
    private final List<String> header = new ArrayList<>();
    private final List<List<String>> rows = new ArrayList<>();
    private String borderChar = "|";
    private String separatorChar = "-";
    private String cornerChar = "+";

    public Table(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets the table header columns.
     */
    public Table header(String... columns) {
        header.clear();
        for (String c : columns) {
            header.add(c != null ? c : "");
        }
        return this;
    }

    /**
     * Adds a data row.
     */
    public Table row(String... cells) {
        List<String> row = new ArrayList<>();
        for (String c : cells) {
            row.add(c != null ? c : "");
        }
        rows.add(row);
        return this;
    }

    /**
     * Prints the table to the given stream.
     */
    public void print(PrintStream out) {
        int cols = header.isEmpty() ? (rows.isEmpty() ? 0 : rows.get(0).size()) : header.size();
        if (cols == 0) return;

        int[] widths = new int[cols];
        for (int i = 0; i < header.size() && i < cols; i++) {
            widths[i] = Math.max(widths[i], cellWidth(header.get(i)));
        }
        for (List<String> row : rows) {
            for (int i = 0; i < row.size() && i < cols; i++) {
                widths[i] = Math.max(widths[i], cellWidth(row.get(i)));
            }
        }
        for (int i = 0; i < cols; i++) {
            widths[i] = Math.max(1, widths[i]);
        }

        StringBuilder sep = new StringBuilder(cornerChar);
        for (int w : widths) {
            sep.append(repeat(separatorChar, w + 2)).append(cornerChar);
        }
        String sepLine = sep.toString();

        if (support.isAnsiEnabled()) {
            out.println(Ansi.BOLD + sepLine + Ansi.RESET);
        } else {
            out.println(sepLine);
        }

        if (!header.isEmpty()) {
            printRow(out, header, widths, true);
            out.println(sepLine);
        }

        for (List<String> row : rows) {
            List<String> padded = new ArrayList<>();
            for (int i = 0; i < cols; i++) {
                padded.add(i < row.size() ? row.get(i) : "");
            }
            printRow(out, padded, widths, false);
        }
        out.println(sepLine);
    }

    /**
     * Prints the table to stdout.
     */
    public void print() {
        print(System.out);
    }

    private void printRow(PrintStream out, List<String> row, int[] widths, boolean bold) {
        if (support.isAnsiEnabled() && bold) {
            out.print(Ansi.BOLD);
        }
        out.print(borderChar);
        for (int i = 0; i < row.size(); i++) {
            String cell = row.get(i);
            int w = i < widths.length ? widths[i] : cellWidth(cell);
            out.print(" ");
            out.print(pad(cell, w));
            out.print(" ");
            out.print(borderChar);
        }
        out.println();
        if (support.isAnsiEnabled() && bold) {
            out.print(Ansi.RESET);
        }
    }

    private static int cellWidth(String s) {
        int len = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            len += (c >= 0x1100 && Character.getType(c) == Character.OTHER_LETTER) ? 2 : 1;
        }
        return len;
    }

    private static String pad(String s, int width) {
        int w = cellWidth(s);
        if (w >= width) return s;
        return s + " ".repeat(width - w);
    }

    private static String repeat(String s, int n) {
        return s.repeat(Math.max(0, n));
    }
}
