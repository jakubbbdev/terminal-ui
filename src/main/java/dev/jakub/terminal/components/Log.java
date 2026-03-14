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
    private Level minLevel;

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
     * Only print entries at this level or higher (e.g. WARN shows WARN and ERROR).
     * Useful for "quiet" mode or production.
     */
    public Log minLevel(Level level) {
        this.minLevel = level;
        return this;
    }

    /**
     * Prints log entries to the given stream (respects minLevel if set).
     */
    public void print(PrintStream out) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
        for (Entry e : entries) {
            if (minLevel != null && e.level.ordinal() < minLevel.ordinal()) continue;
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

    /**
     * Log level for filtering. Order: DEBUG &lt; INFO &lt; WARN &lt; ERROR.
     */
    public enum Level {
        DEBUG(Ansi.FG_BRIGHT_BLACK),
        INFO(Ansi.FG_CYAN),
        WARN(Ansi.FG_YELLOW),
        ERROR(Ansi.FG_RED);

        final String ansi;

        Level(String ansi) {
            this.ansi = ansi;
        }
    }
}
