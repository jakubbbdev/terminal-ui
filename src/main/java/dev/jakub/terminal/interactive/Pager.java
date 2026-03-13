package dev.jakub.terminal.interactive;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Pager for long lists. Use via {@link Terminal#pager()}.
 * Interactive: Enter/Space/Down/Right = next page, Up/Left = previous, 'q' = quit.
 */
public final class Pager {

    private final TerminalSupport support;
    private final List<String> lines = new ArrayList<>();
    private int pageSize = 20;
    private PrintStream out = System.out;
    private InputStream in = System.in;

    public Pager(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets the lines to display.
     */
    public Pager lines(List<String> lines) {
        this.lines.clear();
        if (lines != null) {
            this.lines.addAll(lines);
        }
        return this;
    }

    /**
     * Sets the lines (varargs).
     */
    public Pager lines(String... lines) {
        this.lines.clear();
        if (lines != null) {
            for (String s : lines) this.lines.add(s != null ? s : "");
        }
        return this;
    }

    /**
     * Sets page size (default 20).
     */
    public Pager pageSize(int size) {
        this.pageSize = Math.max(1, size);
        return this;
    }

    /**
     * Sets output stream.
     */
    public Pager output(PrintStream out) {
        this.out = out != null ? out : System.out;
        return this;
    }

    /**
     * Sets input stream (for tests).
     */
    public Pager input(InputStream in) {
        this.in = in != null ? in : System.in;
        return this;
    }

    /**
     * Shows all lines at once (no interaction).
     */
    public void print(PrintStream out) {
        for (String line : lines) {
            out.println(line);
        }
    }

    /**
     * Interactive paging: Enter/Space/Down/Right = next, Up/Left = previous, 'q' = quit.
     */
    public void interactive() {
        int totalPages = Math.max(1, (lines.size() + pageSize - 1) / pageSize);
        int page = 0;
        while (true) {
            int from = page * pageSize;
            int to = Math.min(from + pageSize, lines.size());
            for (int i = from; i < to; i++) {
                out.println(lines.get(i));
            }
            out.println();
            out.print("[Page " + (page + 1) + " / " + totalPages + "]  Enter/Space/Down=next, Up=prev, q=quit: ");
            out.flush();

            switch (readKey(in)) {
                case NEXT:
                    page++;
                    if (page >= totalPages) return;
                    break;
                case PREV:
                    if (page > 0) page--;
                    break;
                case QUIT:
                    return;
            }
        }
    }

    private enum KeyAction { NEXT, PREV, QUIT }

    private static KeyAction readKey(InputStream in) {
        try {
            int c = in.read();
            if (c == -1) return KeyAction.QUIT;
            if (c == 'q' || c == 'Q') return KeyAction.QUIT;
            if (c == ' ' || c == '\n' || c == '\r') return KeyAction.NEXT;
            if (c == '\u001b') {
                if (in.read() != '[') return KeyAction.NEXT;
                while (true) {
                    int b = in.read();
                    if (b == -1) return KeyAction.QUIT;
                    if (b == 'A') return KeyAction.PREV;
                    if (b == 'B' || b == 'C') return KeyAction.NEXT;
                    if (b == 'D') return KeyAction.PREV;
                    if (b >= 0x40) return KeyAction.NEXT;
                }
            }
            return KeyAction.NEXT;
        } catch (Exception e) {
            return KeyAction.QUIT;
        }
    }

    /**
     * Prints all to stdout.
     */
    public void print() {
        print(System.out);
    }
}
