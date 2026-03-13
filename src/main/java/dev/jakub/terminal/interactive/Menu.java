package dev.jakub.terminal.interactive;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Fluent builder for interactive terminal menus. Use via {@link Terminal#menu()}.
 */
public final class Menu {

    private final TerminalSupport support;
    private final List<String> options = new ArrayList<>();
    private String title = "Select an option";
    private PrintStream out = System.out;
    private java.io.InputStream in = System.in;

    public Menu(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets the menu title.
     */
    public Menu title(String title) {
        this.title = title != null ? title : "";
        return this;
    }

    /**
     * Adds an option. Order is preserved.
     */
    public Menu option(String option) {
        options.add(option != null ? option : "");
        return this;
    }

    /**
     * Sets the output stream (default: stdout).
     */
    public Menu output(PrintStream out) {
        this.out = out != null ? out : System.out;
        return this;
    }

    /**
     * Sets the input stream (default: stdin). Useful for testing.
     */
    public Menu input(java.io.InputStream in) {
        this.in = in != null ? in : System.in;
        return this;
    }

    /**
     * Displays the menu and blocks until the user selects an option.
     * Returns the selected option string, or the first option if only one,
     * or null if no options or read fails.
     */
    public String select() {
        if (options.isEmpty()) {
            return null;
        }
        if (options.size() == 1) {
            return options.get(0);
        }

        if (support.isAnsiEnabled()) {
            out.println(Ansi.BOLD + title + Ansi.RESET);
            out.println();
        } else {
            out.println(title);
            out.println();
        }

        for (int i = 0; i < options.size(); i++) {
            out.println("  " + (i + 1) + ". " + options.get(i));
        }
        out.println();
        out.print("Enter choice (1-" + options.size() + "): ");
        out.flush();

        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line == null) continue;
                line = line.trim();
                try {
                    int choice = Integer.parseInt(line);
                    if (choice >= 1 && choice <= options.size()) {
                        return options.get(choice - 1);
                    }
                } catch (NumberFormatException ignored) {}
                out.print("Invalid. Enter choice (1-" + options.size() + "): ");
                out.flush();
            }
        }
        return null;
    }
}
