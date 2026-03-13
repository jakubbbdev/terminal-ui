package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Breadcrumb path. Use via {@link Terminal#breadcrumb()}.
 * Last crumb bold with ANSI. Configurable separator.
 */
public final class Breadcrumb {

    private final TerminalSupport support;
    private final List<String> crumbs = new ArrayList<>();
    private String separator = " > ";

    public Breadcrumb(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds a crumb.
     */
    public Breadcrumb crumb(String label) {
        crumbs.add(label != null ? label : "");
        return this;
    }

    /**
     * Sets the separator (default " > ").
     */
    public Breadcrumb separator(String sep) {
        this.separator = sep != null ? sep : " ";
        return this;
    }

    /**
     * Prints the breadcrumb to the given stream.
     */
    public void print(PrintStream out) {
        for (int i = 0; i < crumbs.size(); i++) {
            if (i > 0) out.print(separator);
            boolean last = (i == crumbs.size() - 1);
            if (last && support.isAnsiEnabled()) {
                out.print(Ansi.BOLD);
            }
            out.print(crumbs.get(i));
            if (last && support.isAnsiEnabled()) {
                out.print(Ansi.RESET);
            }
        }
        out.println();
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }
}
