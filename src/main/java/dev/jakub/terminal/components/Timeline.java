package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Vertical timeline with events. Use via {@link Terminal#timeline()}.
 * Dots and connectors; with ANSI dots colored cyan/green.
 */
public final class Timeline {

    private static final String DOT_UTF8 = "●";
    private static final String PIPE_UTF8 = "│";
    private static final String DOT_ASCII = "*";
    private static final String PIPE_ASCII = "|";

    private final TerminalSupport support;
    private final List<Event> events = new ArrayList<>();

    public Timeline(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds an event (label + description).
     */
    public Timeline event(String label, String description) {
        events.add(new Event(label != null ? label : "", description != null ? description : ""));
        return this;
    }

    /**
     * Prints the timeline to the given stream.
     */
    public void print(PrintStream out) {
        boolean ascii = !support.isUtf8Symbols();
        String dot = ascii ? DOT_ASCII : DOT_UTF8;
        String pipe = ascii ? PIPE_ASCII : PIPE_UTF8;
        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            if (support.isAnsiEnabled()) {
                out.print(i == events.size() - 1 ? Ansi.FG_GREEN : Ansi.FG_CYAN);
            }
            out.print(dot + " ");
            if (support.isAnsiEnabled()) out.print(Ansi.RESET);
            out.println(e.label + "  " + e.description);
            if (i < events.size() - 1) {
                if (support.isAnsiEnabled()) out.print(Ansi.FG_CYAN);
                out.println(pipe);
                if (support.isAnsiEnabled()) out.print(Ansi.RESET);
            }
        }
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }

    private static final class Event {
        final String label;
        final String description;

        Event(String label, String description) {
            this.label = label;
            this.description = description;
        }
    }
}
