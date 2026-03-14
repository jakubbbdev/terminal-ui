package dev.jakub.terminal.core;

public final class Ansi {

    /** Reset all attributes */
    public static final String RESET = "\u001B[0m";

    /** Bold/bright */
    public static final String BOLD = "\u001B[1m";
    /** Dim */
    public static final String DIM = "\u001B[2m";
    /** Italic */
    public static final String ITALIC = "\u001B[3m";
    /** Underline */
    public static final String UNDERLINE = "\u001B[4m";

    /** Black foreground */
    public static final String FG_BLACK = "\u001B[30m";
    /** Red foreground */
    public static final String FG_RED = "\u001B[31m";
    /** Green foreground */
    public static final String FG_GREEN = "\u001B[32m";
    /** Yellow foreground */
    public static final String FG_YELLOW = "\u001B[33m";
    /** Blue foreground */
    public static final String FG_BLUE = "\u001B[34m";
    /** Magenta foreground */
    public static final String FG_MAGENTA = "\u001B[35m";
    /** Cyan foreground */
    public static final String FG_CYAN = "\u001B[36m";
    /** White foreground */
    public static final String FG_WHITE = "\u001B[37m";

    /** Bright black (gray) foreground */
    public static final String FG_BRIGHT_BLACK = "\u001B[90m";
    /** Bright red foreground */
    public static final String FG_BRIGHT_RED = "\u001B[91m";
    /** Bright green foreground */
    public static final String FG_BRIGHT_GREEN = "\u001B[92m";
    /** Bright yellow foreground */
    public static final String FG_BRIGHT_YELLOW = "\u001B[93m";
    /** Bright blue foreground */
    public static final String FG_BRIGHT_BLUE = "\u001B[94m";
    /** Bright magenta foreground */
    public static final String FG_BRIGHT_MAGENTA = "\u001B[95m";
    /** Bright cyan foreground */
    public static final String FG_BRIGHT_CYAN = "\u001B[96m";
    /** Bright white foreground */
    public static final String FG_BRIGHT_WHITE = "\u001B[97m";

    /** Hide cursor */
    public static final String HIDE_CURSOR = "\u001B[?25l";
    /** Show cursor */
    public static final String SHOW_CURSOR = "\u001B[?25h";
    /** Move cursor up one line */
    public static final String CURSOR_UP = "\u001B[1A";
    /** Move cursor down one line */
    public static final String CURSOR_DOWN = "\u001B[1B";
    /** Carriage return (start of line) */
    public static final String CARRIAGE_RETURN = "\r";
    /** Clear line */
    public static final String ERASE_LINE = "\u001B[2K";

    /** Clear entire screen and move cursor to home (1,1) */
    public static final String CLEAR_SCREEN = "\u001B[H\u001B[2J";

    /**
     * Cursor position (1-based row and column). ESC [ row ; col H
     */
    public static String cursorTo(int row, int col) {
        return "\u001B[" + Math.max(1, row) + ";" + Math.max(1, col) + "H";
    }

    private Ansi() {}
}
