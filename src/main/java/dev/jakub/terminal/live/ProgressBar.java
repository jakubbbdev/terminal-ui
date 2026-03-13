package dev.jakub.terminal.live;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe progress bar. Use via {@link Terminal#progressBar()}.
 */
public final class ProgressBar {

    /**
     * Visual style for the progress bar fill.
     */
    public enum Style {
        /** Block characters: [████████░░░░] */
        BLOCK("█", "░"),
        /** Equals: [========----] */
        EQUALS("=", "-"),
        /** Hash: [########    ] */
        HASH("#", " "),
        /** Arrow: [>>>>>>------] */
        ARROW(">", "-");

        private final String filled;
        private final String empty;

        Style(String filled, String empty) {
            this.filled = filled;
            this.empty = empty;
        }
    }

    private final long total;
    private final int width;
    private final Style style;
    private final TerminalSupport support;
    private final AtomicLong current = new AtomicLong(0);
    private final ReentrantLock printLock = new ReentrantLock();
    private final PrintStream out;
    private boolean printed;
    private String prefix = "";
    private String suffix = "";

    public ProgressBar(long total, int width, Style style, TerminalSupport support, PrintStream out) {
        this.total = Math.max(1, total);
        this.width = Math.max(10, Math.min(width, 200));
        this.style = style != null ? style : Style.BLOCK;
        this.support = support;
        this.out = out != null ? out : System.out;
    }

    /**
     * Sets optional text before the bar.
     */
    public ProgressBar prefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
        return this;
    }

    /**
     * Sets optional text after the bar (e.g. percentage).
     */
    public ProgressBar suffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
        return this;
    }

    /**
     * Updates the current value and redraws the bar. Thread-safe.
     */
    public void update(long value) {
        current.set(Math.max(0, Math.min(value, total)));
        redraw();
    }

    /**
     * Increments the current value by one and redraws. Thread-safe.
     */
    public void increment() {
        long next = current.updateAndGet(v -> Math.min(v + 1, total));
        redraw();
    }

    /**
     * Returns the current progress value.
     */
    public long getCurrent() {
        return current.get();
    }

    /**
     * Returns the total (max) value.
     */
    public long getTotal() {
        return total;
    }

    private void redraw() {
        printLock.lock();
        try {
            long cur = current.get();
            int filledCount = (int) ((cur * width) / total);
            filledCount = Math.max(0, Math.min(filledCount, width));
            String filledCh = support.isUtf8Symbols() ? style.filled : asciiFilled();
            String emptyCh = support.isUtf8Symbols() ? style.empty : asciiEmpty();
            String filled = filledCh.repeat(filledCount);
            String empty = emptyCh.repeat(width - filledCount);
            String bar = "[" + filled + empty + "]";
            int pct = total > 0 ? (int) (100 * cur / total) : 0;
            String line = prefix + bar + " " + pct + "%" + suffix;
            if (printed) {
                out.print(Ansi.CARRIAGE_RETURN);
                if (support.isAnsiEnabled()) {
                    out.print(Ansi.ERASE_LINE);
                }
            }
            out.print(line);
            printed = true;
        } finally {
            printLock.unlock();
        }
    }

    private String asciiFilled() {
        return switch (style) {
            case BLOCK -> "#";
            case EQUALS -> "=";
            case HASH -> "#";
            case ARROW -> ">";
        };
    }

    private String asciiEmpty() {
        return switch (style) {
            case BLOCK, HASH -> " ";
            case EQUALS, ARROW -> "-";
        };
    }

    /**
     * Completes the bar at 100% and prints a newline.
     */
    public void complete() {
        update(total);
        printLock.lock();
        try {
            out.println();
        } finally {
            printLock.unlock();
        }
    }
}
