package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Step / wizard display. Use via {@link Terminal#steps()}.
 * Status: DONE, RUNNING, PENDING, FAILED. Icons with ASCII fallback.
 */
public final class Steps {

    private static final String ICON_DONE_UTF8 = "✅";
    private static final String ICON_RUNNING_UTF8 = "⏳";
    private static final String ICON_PENDING_UTF8 = "○";
    private static final String ICON_FAILED_UTF8 = "❌";
    private static final String ICON_DONE_ASCII = "[x]";
    private static final String ICON_RUNNING_ASCII = "[~]";
    private static final String ICON_PENDING_ASCII = "[ ]";
    private static final String ICON_FAILED_ASCII = "[!]";

    private final TerminalSupport support;
    private final List<StepEntry> steps = new ArrayList<>();

    public Steps(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds a step with status.
     */
    public Steps step(String label, Status status) {
        steps.add(new StepEntry(label != null ? label : "", status != null ? status : Status.PENDING));
        return this;
    }

    /**
     * Prints all steps to the given stream.
     */
    public void print(PrintStream out) {
        boolean ascii = !support.isUtf8Symbols();
        for (StepEntry e : steps) {
            String icon = icon(e.status, ascii);
            if (support.isAnsiEnabled()) {
                out.print(colorFor(e.status));
            }
            out.print(icon + " ");
            if (support.isAnsiEnabled()) {
                out.print(Ansi.RESET);
            }
            out.println(e.label);
        }
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }

    private static String icon(Status s, boolean ascii) {
        if (ascii) {
            return switch (s) {
                case DONE -> ICON_DONE_ASCII;
                case RUNNING -> ICON_RUNNING_ASCII;
                case PENDING -> ICON_PENDING_ASCII;
                case FAILED -> ICON_FAILED_ASCII;
            };
        }
        return switch (s) {
            case DONE -> ICON_DONE_UTF8;
            case RUNNING -> ICON_RUNNING_UTF8;
            case PENDING -> ICON_PENDING_UTF8;
            case FAILED -> ICON_FAILED_UTF8;
        };
    }

    private static String colorFor(Status s) {
        return switch (s) {
            case DONE -> Ansi.FG_GREEN;
            case RUNNING -> Ansi.FG_YELLOW;
            case PENDING -> Ansi.FG_WHITE;
            case FAILED -> Ansi.FG_RED;
        };
    }

    private static final class StepEntry {
        final String label;
        final Status status;

        StepEntry(String label, Status status) {
            this.label = label;
            this.status = status;
        }
    }

    /**
     * Step status for {@link Steps}.
     */
    public enum Status {
        DONE, RUNNING, PENDING, FAILED
    }
}
