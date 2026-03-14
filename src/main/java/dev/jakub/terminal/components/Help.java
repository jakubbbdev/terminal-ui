package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * CLI help / usage block. Use via {@link Terminal#help()}.
 * Formats options and descriptions for a typical --help output.
 */
public final class Help {

    private static final int DEFAULT_OPTION_WIDTH = 24;

    private final TerminalSupport support;
    private final List<String> options = new ArrayList<>();
    private final List<String> descriptions = new ArrayList<>();
    private String title = "Usage";
    private int optionWidth = DEFAULT_OPTION_WIDTH;
    private PrintStream out = System.out;

    public Help(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets the title (e.g. "Usage" or "Options").
     */
    public Help title(String title) {
        this.title = title != null ? title : "";
        return this;
    }

    /**
     * Adds an option with description (e.g. "-v, --verbose" and "Enable verbose output").
     */
    public Help option(String option, String description) {
        options.add(option != null ? option : "");
        descriptions.add(description != null ? description : "");
        return this;
    }

    /**
     * Sets the width of the option column (default 24).
     */
    public Help optionWidth(int width) {
        this.optionWidth = Math.max(8, width);
        return this;
    }

    /**
     * Sets output stream (default: stdout).
     */
    public Help output(PrintStream out) {
        this.out = out != null ? out : System.out;
        return this;
    }

    /**
     * Prints the help block to the given stream.
     */
    public void print(PrintStream stream) {
        if (stream == null) stream = System.out;
        if (!title.isEmpty()) {
            stream.println(title);
            stream.println();
        }
        int ow = Math.max(optionWidth, 8);
        for (int i = 0; i < options.size(); i++) {
            String opt = options.get(i);
            String desc = i < descriptions.size() ? descriptions.get(i) : "";
            String padded = opt.length() <= ow ? opt + " ".repeat(ow - opt.length()) : opt;
            stream.print("  " + padded + "  ");
            stream.println(desc);
        }
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(out);
    }
}
