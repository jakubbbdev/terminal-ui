package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;

/**
 * Line-by-line diff viewer. Use via {@link Terminal#diff()}.
 * Red for removed, green for added.
 */
public final class Diff {

    private final TerminalSupport support;
    private String before = "";
    private String after = "";

    public Diff(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets the "before" (old) text. Removed lines will be shown in red with "-".
     */
    public Diff before(String text) {
        this.before = text != null ? text : "";
        return this;
    }

    /**
     * Sets the "after" (new) text. Added lines will be shown in green with "+".
     */
    public Diff after(String text) {
        this.after = text != null ? text : "";
        return this;
    }

    /**
     * Prints a simple line-by-line diff to the given stream.
     */
    public void print(PrintStream out) {
        String[] a = before.split("\\n", -1);
        String[] b = after.split("\\n", -1);
        int i = 0, j = 0;
        while (i < a.length || j < b.length) {
            if (i < a.length && j < b.length && a[i].equals(b[j])) {
                if (support.isAnsiEnabled()) out.print(Ansi.FG_WHITE);
                out.println("  " + a[i]);
                if (support.isAnsiEnabled()) out.print(Ansi.RESET);
                i++;
                j++;
            } else if (j < b.length && (i >= a.length || !containsAt(a, b[j], i))) {
                if (support.isAnsiEnabled()) out.print(Ansi.FG_GREEN);
                out.println("+ " + b[j]);
                if (support.isAnsiEnabled()) out.print(Ansi.RESET);
                j++;
            } else if (i < a.length) {
                if (support.isAnsiEnabled()) out.print(Ansi.FG_RED);
                out.println("- " + a[i]);
                if (support.isAnsiEnabled()) out.print(Ansi.RESET);
                i++;
            }
        }
    }

    private boolean containsAt(String[] a, String line, int from) {
        for (int k = from; k < a.length; k++) {
            if (a[k].equals(line)) return true;
        }
        return false;
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }
}
