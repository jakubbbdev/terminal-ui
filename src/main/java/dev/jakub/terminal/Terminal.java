package dev.jakub.terminal;

import dev.jakub.terminal.components.*;
import dev.jakub.terminal.core.Color;
import dev.jakub.terminal.core.StyledText;
import dev.jakub.terminal.core.TerminalSupport;
import dev.jakub.terminal.interactive.Confirm;
import dev.jakub.terminal.interactive.Menu;
import dev.jakub.terminal.interactive.Pager;
import dev.jakub.terminal.interactive.SelectList;
import dev.jakub.terminal.internal.PromptBuilder;
import dev.jakub.terminal.live.Dashboard;
import dev.jakub.terminal.live.ProgressBarBuilder;
import dev.jakub.terminal.live.SpinnerBuilder;

public final class Terminal {

    private static final TerminalSupport DEFAULT_SUPPORT = new TerminalSupport();

    private final TerminalSupport support;

    /**
     * Creates a Terminal instance with auto-detected capabilities.
     */
    public Terminal() {
        this.support = DEFAULT_SUPPORT;
    }

    /**
     * Creates a Terminal instance with the given support (e.g. for testing).
     */
    public Terminal(TerminalSupport support) {
        this.support = support != null ? support : DEFAULT_SUPPORT;
    }

    /**
     * Shared default Terminal instance.
     */
    private static final Terminal DEFAULT = new Terminal();

    /**
     * Returns a new table builder using default terminal support.
     */
    public static Table table() {
        return DEFAULT.tableInternal();
    }

    /**
     * Returns a new table builder with the given terminal support (e.g. for testing).
     */
    public static Table table(TerminalSupport support) {
        return new Table(support != null ? support : DEFAULT.getSupport());
    }

    /**
     * Returns a styled text builder for the given string. Chain with
     * {@link StyledText#color(Color)}, {@link StyledText#bold()}, etc.,
     * then {@link StyledText#print()} or {@link StyledText#println()}.
     */
    public static StyledText print(String text) {
        return new StyledText(text, DEFAULT.getSupport());
    }

    /**
     * Returns a progress bar builder.
     */
    public static ProgressBarBuilder progressBar() {
        return new ProgressBarBuilder(DEFAULT.getSupport());
    }

    /**
     * Returns a spinner builder. Call {@link SpinnerBuilder#start()} to run.
     */
    public static SpinnerBuilder spinner() {
        return new SpinnerBuilder(DEFAULT.getSupport());
    }

    /**
     * Returns an interactive menu builder. Call {@link Menu#select()} to show and get choice.
     */
    public static Menu menu() {
        return new Menu(DEFAULT.getSupport());
    }

    /**
     * Returns a select list (arrow keys Up/Down + Enter, or number + Enter). Call {@link SelectList#select()}.
     */
    public static SelectList selectList() {
        return new SelectList(DEFAULT.getSupport());
    }

    /**
     * Returns a horizontal rule/separator builder.
     */
    public static Rule rule() {
        return new Rule(DEFAULT.getSupport());
    }

    /**
     * Returns a key-value list builder (aligned labels).
     */
    public static KeyValue keyValue() {
        return new KeyValue(DEFAULT.getSupport());
    }

    /**
     * Returns a box builder (text in a frame).
     */
    public static Box box() {
        return new Box(DEFAULT.getSupport());
    }

    /** Returns a tree builder. Use {@link Tree#node(String)} then {@link Tree.TreeBuilder#child(String)} / {@link Tree.TreeBuilder#end()}. */
    public static Tree tree() {
        return new Tree(DEFAULT.getSupport());
    }

    /** Returns a multi-column layout (2–4 columns). */
    public static Columns columns() {
        return new Columns(DEFAULT.getSupport());
    }

    /** Returns a prompt builder. Chain {@link PromptBuilder#masked()} then {@link PromptBuilder#ask()}. */
    public static PromptBuilder prompt(String message) {
        return new PromptBuilder(message, DEFAULT.getSupport());
    }

    /** Returns a colored badge. Chain {@link Badge#print()} or {@link Badge#println()}. */
    public static Badge badge(String label, Color color) {
        return new Badge(label, color, DEFAULT.getSupport());
    }

    /** Returns a diff viewer (before/after). */
    public static Diff diff() {
        return new Diff(DEFAULT.getSupport());
    }

    /** Returns a log viewer (info, warn, error, debug). */
    public static Log log() {
        return new Log(DEFAULT.getSupport());
    }

    /** Returns a timeline builder. */
    public static Timeline timeline() {
        return new Timeline(DEFAULT.getSupport());
    }

    /** Returns a heatmap builder. */
    public static Heatmap heatmap() {
        return new Heatmap(DEFAULT.getSupport());
    }

    /** Returns an ASCII chart / sparkline builder. */
    public static Chart chart() {
        return new Chart(DEFAULT.getSupport());
    }

    /** Returns a yes/no confirm dialog. */
    public static Confirm confirm(String message) {
        return new Confirm(message, DEFAULT.getSupport());
    }

    /** Returns a steps/wizard display builder. */
    public static Steps steps() {
        return new Steps(DEFAULT.getSupport());
    }

    /** Returns a breadcrumb builder. */
    public static Breadcrumb breadcrumb() {
        return new Breadcrumb(DEFAULT.getSupport());
    }

    /** Prints a notification (SUCCESS, WARNING, ERROR, INFO). */
    public static void notify(String message, Notification.Type type) {
        new Notification(message, type, DEFAULT.getSupport()).print();
    }

    /** Returns a code block builder (optional syntax highlighting). */
    public static CodeBlock code(String language) {
        return new CodeBlock(language, DEFAULT.getSupport());
    }

    /** Returns a system info widget. */
    public static SysInfo sysinfo() {
        return new SysInfo(DEFAULT.getSupport());
    }

    /** Returns a lightweight Markdown renderer. */
    public static Markdown markdown(String source) {
        return new Markdown(source, DEFAULT.getSupport());
    }

    /** Returns a table-of-contents builder. */
    public static TableOfContents toc() {
        return new TableOfContents(DEFAULT.getSupport());
    }

    /** Returns a live dashboard (refresh with widgets). */
    public static Dashboard dashboard() {
        return new Dashboard(DEFAULT.getSupport());
    }

    /** Returns a pager for long lists. */
    public static Pager pager() {
        return new Pager(DEFAULT.getSupport());
    }

    /** Returns a help/usage builder for CLI --help output. */
    public static Help help() {
        return new Help(DEFAULT.getSupport());
    }

    /** Clears the terminal screen (ANSI). No-op if ANSI is disabled. */
    public static void clearScreen() {
        if (DEFAULT.getSupport().isAnsiEnabled()) {
            System.out.print(dev.jakub.terminal.core.Ansi.CLEAR_SCREEN);
            System.out.flush();
        }
    }

    /** Moves the cursor to the given 1-based row and column (ANSI). No-op if ANSI is disabled. */
    public static void cursorTo(int row, int col) {
        if (DEFAULT.getSupport().isAnsiEnabled()) {
            System.out.print(dev.jakub.terminal.core.Ansi.cursorTo(row, col));
            System.out.flush();
        }
    }

    TerminalSupport getSupport() {
        return support;
    }

    Table tableInternal() {
        return new Table(support);
    }
}
