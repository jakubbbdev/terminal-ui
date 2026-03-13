package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Log viewer with level labels and colors. Use via {@link Terminal#log()}.
 * INFO=cyan, WARN=yellow, ERROR=red, DEBUG=gray.
 */
public final class Log {

    private static final int LABEL_WIDTH = 6;

    private final TerminalSupport support;
    private final List<Entry> entries = new ArrayList<>();
    private boolean withTimestamp;

    public Log(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds an INFO line (cyan).
     */
    public Log info(String message) {
        entries.add(new Entry(Level.INFO, message));
        return this;
    }

    /**
     * Adds a WARN line (yellow).
     */
    public Log warn(String message) {
        entries.add(new Entry(Level.WARN, message));
        return this;
    }

    /**
     * Adds an ERROR line (red).
     */
    public Log error(String message) {
        entries.add(new Entry(Level.ERROR, message));
        return this;
    }

    /**
     * Adds a DEBUG line (gray).
     */
    public Log debug(String message) {
        entries.add(new Entry(Level.DEBUG, message));
        return this;
    }

    /**
     * Enables timestamp prefix on each line.
     */
    public Log withTimestamp() {
        this.withTimestamp = true;
        return this;
    }

    /**
     * Prints all log entries to the given stream.
     */
    public void print(PrintStream out) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
        for (Entry e : entries) {
            String prefix = withTimestamp ? "[" + fmt.format(Instant.now()) + "] " : "";
            String label = "[" + e.level.name() + "]";
            while (label.length() < LABEL_WIDTH) label += " ";
            if (support.isAnsiEnabled()) {
                out.print(e.level.ansi);
            }
            out.print(prefix + label + "  " + e.message);
            if (support.isAnsiEnabled()) {
                out.print(Ansi.RESET);
            }
            out.println();
        }
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }

    private static final class Entry {
        final Level level;
        final String message;

        Entry(Level level, String message) {
            this.level = level;
            this.message = message != null ? message : "";
        }
    }

    private enum Level {
        INFO(Ansi.FG_CYAN),
        WARN(Ansi.FG_YELLOW),
        ERROR(Ansi.FG_RED),
        DEBUG(Ansi.FG_BRIGHT_BLACK);

        final String ansi;

        Level(String ansi) {
            this.ansi = ansi;
        }
    }
}
