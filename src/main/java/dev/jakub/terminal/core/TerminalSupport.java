package dev.jakub.terminal.core;

/**
 * Detects terminal capabilities: width and ANSI support.
 * Graceful fallback when ANSI is not supported (e.g. Windows CMD).
 */
public final class TerminalSupport {

    private static final int DEFAULT_WIDTH = 80;
    private static final String COLUMNS_ENV = "COLUMNS";
    private static final String TERM_ENV = "TERM";
    private static final String ANSI_PROP = "dev.jakub.terminal.ansi";
    private static final String UTF8_PROP = "dev.jakub.terminal.utf8";

    private final boolean ansiEnabled;
    private final int width;
    private final boolean utf8Symbols;

    /**
     * Creates support with auto-detected capabilities.
     */
    public TerminalSupport() {
        this.ansiEnabled = detectAnsiSupport();
        this.width = detectTerminalWidth();
        this.utf8Symbols = detectUtf8Symbols();
    }

    /**
     * Creates support with explicit ANSI and width (for testing).
     */
    public TerminalSupport(boolean ansiEnabled, int width) {
        this.ansiEnabled = ansiEnabled;
        this.width = Math.max(10, width);
        this.utf8Symbols = false;
    }

    /**
     * Creates support with explicit ANSI, width and UTF-8 symbols (for testing).
     */
    public TerminalSupport(boolean ansiEnabled, int width, boolean utf8Symbols) {
        this.ansiEnabled = ansiEnabled;
        this.width = Math.max(10, width);
        this.utf8Symbols = utf8Symbols;
    }

    private static boolean detectUtf8Symbols() {
        String force = System.getProperty(UTF8_PROP);
        if ("true".equalsIgnoreCase(force)) return true;
        if ("false".equalsIgnoreCase(force)) return false;
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) return false;
        String encoding = System.getProperty("sun.stdout.encoding", System.getProperty("file.encoding", ""));
        return encoding.toUpperCase().contains("UTF-8");
    }

    private static boolean detectAnsiSupport() {
        String force = System.getProperty(ANSI_PROP);
        if ("true".equalsIgnoreCase(force)) return true;
        if ("false".equalsIgnoreCase(force)) return false;

        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            String ver = System.getProperty("os.version", "");
            try {
                int major = Integer.parseInt(ver.split("\\.")[0]);
                if (major >= 10) return true;
            } catch (Exception ignored) {}
            return false;
        }

        if (System.console() != null) {
            String term = System.getenv(TERM_ENV);
            return term != null && !term.isEmpty() && !term.equalsIgnoreCase("dumb");
        }
        return false;
    }

    private static int detectTerminalWidth() {
        String columns = System.getenv(COLUMNS_ENV);
        if (columns != null && !columns.isEmpty()) {
            try {
                int w = Integer.parseInt(columns.trim());
                if (w > 0) return w;
            } catch (NumberFormatException ignored) {}
        }
        return DEFAULT_WIDTH;
    }

    /**
     * Whether ANSI escape codes should be emitted.
     */
    public boolean isAnsiEnabled() {
        return ansiEnabled;
    }

    /**
     * Detected or configured terminal width in columns.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Whether to use UTF-8 symbols (box-drawing, emoji, blocks). When false, ASCII fallback is used
     * so output looks correct in Windows CMD/PowerShell without UTF-8 encoding.
     */
    public boolean isUtf8Symbols() {
        return utf8Symbols;
    }
}
