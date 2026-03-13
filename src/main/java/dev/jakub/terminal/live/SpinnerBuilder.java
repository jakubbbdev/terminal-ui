package dev.jakub.terminal.live;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;

/**
 * Fluent builder for {@link Spinner}. Use via {@link Terminal#spinner()}.
 */
public final class SpinnerBuilder {

    private final TerminalSupport support;
    private String message = "Loading...";
    private PrintStream out = System.out;

    public SpinnerBuilder(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets the message shown next to the spinner.
     */
    public SpinnerBuilder message(String message) {
        this.message = message != null ? message : "";
        return this;
    }

    /**
     * Sets the output stream (default: stdout).
     */
    public SpinnerBuilder output(PrintStream out) {
        this.out = out != null ? out : System.out;
        return this;
    }

    /**
     * Builds and starts the spinner. Returns the running Spinner instance.
     */
    public Spinner start() {
        Spinner s = new Spinner(message, support, out);
        s.start();
        return s;
    }
}
