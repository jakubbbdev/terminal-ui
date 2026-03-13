package dev.jakub.terminal.live;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;

/**
 * Fluent builder for {@link ProgressBar}. Use via {@link Terminal#progressBar()}.
 */
public final class ProgressBarBuilder {

    private final TerminalSupport support;
    private long total = 100;
    private int width = 40;
    private ProgressBar.Style style = ProgressBar.Style.BLOCK;
    private PrintStream out = System.out;

    public ProgressBarBuilder(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets the total (maximum) value for the progress bar.
     */
    public ProgressBarBuilder total(long total) {
        this.total = Math.max(1, total);
        return this;
    }

    /**
     * Sets the width in characters of the bar (excluding brackets and suffix).
     */
    public ProgressBarBuilder width(int width) {
        this.width = Math.max(10, Math.min(width, 200));
        return this;
    }

    /**
     * Sets the visual style of the bar (e.g. BLOCK for [████░░░]).
     */
    public ProgressBarBuilder style(ProgressBar.Style style) {
        this.style = style != null ? style : ProgressBar.Style.BLOCK;
        return this;
    }

    /**
     * Sets the output stream (default: stdout).
     */
    public ProgressBarBuilder output(PrintStream out) {
        this.out = out != null ? out : System.out;
        return this;
    }

    /**
     * Builds the progress bar instance.
     */
    public ProgressBar build() {
        return new ProgressBar(total, width, style, support, out);
    }
}
