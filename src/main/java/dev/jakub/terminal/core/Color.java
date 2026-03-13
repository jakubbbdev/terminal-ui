package dev.jakub.terminal.core;

import dev.jakub.terminal.Terminal;

/**
 * Standard terminal colors for use with {@link Terminal#print(String)} and other components.
 */
public enum Color {
    BLACK(Ansi.FG_BLACK),
    RED(Ansi.FG_RED),
    GREEN(Ansi.FG_GREEN),
    YELLOW(Ansi.FG_YELLOW),
    BLUE(Ansi.FG_BLUE),
    MAGENTA(Ansi.FG_MAGENTA),
    CYAN(Ansi.FG_CYAN),
    WHITE(Ansi.FG_WHITE),
    BRIGHT_BLACK(Ansi.FG_BRIGHT_BLACK),
    BRIGHT_RED(Ansi.FG_BRIGHT_RED),
    BRIGHT_GREEN(Ansi.FG_BRIGHT_GREEN),
    BRIGHT_YELLOW(Ansi.FG_BRIGHT_YELLOW),
    BRIGHT_BLUE(Ansi.FG_BRIGHT_BLUE),
    BRIGHT_MAGENTA(Ansi.FG_BRIGHT_MAGENTA),
    BRIGHT_CYAN(Ansi.FG_BRIGHT_CYAN),
    BRIGHT_WHITE(Ansi.FG_BRIGHT_WHITE);

    private final String ansiCode;

    Color(String ansiCode) {
        this.ansiCode = ansiCode;
    }

    /**
     * Returns the ANSI escape sequence for this color (foreground).
     */
    public String ansiCode() {
        return ansiCode;
    }
}
