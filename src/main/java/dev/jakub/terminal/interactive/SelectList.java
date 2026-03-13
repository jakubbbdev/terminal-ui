package dev.jakub.terminal.interactive;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Interactive select list: options with one highlighted. Navigate with Up/Down arrow keys
 * and Enter to confirm (when terminal supports raw input), or type the option number and Enter.
 * Use via {@link Terminal#selectList()}.
 */
public final class SelectList {

    private static final long FALLBACK_MS = 2500L;

    private final TerminalSupport support;
    private final List<String> options = new ArrayList<>();
    private String title = "Select an option";
    private PrintStream out = System.out;
    private InputStream in = System.in;

    public SelectList(TerminalSupport support) {
        this.support = support;
    }

    public SelectList title(String title) {
        this.title = title != null ? title : "";
        return this;
    }

    public SelectList option(String option) {
        options.add(option != null ? option : "");
        return this;
    }

    public SelectList output(PrintStream out) {
        this.out = out != null ? out : System.out;
        return this;
    }

    public SelectList input(InputStream in) {
        this.in = in != null ? in : System.in;
        return this;
    }

    /**
     * Shows the list and blocks until the user selects. Returns the selected option, or null if empty.
     */
    public String select() {
        if (options.isEmpty()) return null;
        if (options.size() == 1) return options.get(0);

        boolean ansi = support.isAnsiEnabled();
        if (ansi) out.print(Ansi.HIDE_CURSOR);
        try {
            BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
            Thread reader = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        if (in.available() > 0) {
                            int b = in.read();
                            if (b == -1) break;
                            queue.offer(b);
                        } else {
                            Thread.sleep(30);
                        }
                    }
                } catch (Exception ignored) {}
            }, "select-list-reader");
            reader.setDaemon(true);
            reader.start();

            int current = 0;
            long firstInputAt = -1;
            StringBuilder numInput = new StringBuilder();

            draw(current, ansi);

            while (true) {
                Integer b = queue.poll(FALLBACK_MS, TimeUnit.MILLISECONDS);
                if (b == null) {
                    reader.interrupt();
                    return selectWithScanner(current, ansi);
                }
                if (firstInputAt < 0) firstInputAt = System.currentTimeMillis();

                if (b == 13 || b == 10) {
                    if (numInput.length() > 0) {
                        try {
                            int n = Integer.parseInt(numInput.toString());
                            if (n >= 1 && n <= options.size()) return options.get(n - 1);
                        } catch (NumberFormatException ignored) {}
                    }
                    return options.get(current);
                }

                if (b >= '1' && b <= '9') {
                    numInput.append((char) b.intValue());
                    continue;
                }

                // Windows: 224 (or 0) then 72=Up, 80=Down, 75=Left, 77=Right
                if (b == 224 || b == 0) {
                    Integer b2 = queue.poll(150, TimeUnit.MILLISECONDS);
                    if (b2 != null) {
                        if (b2 == 72) {
                            current = (current - 1 + options.size()) % options.size();
                            numInput.setLength(0);
                            redraw(current, ansi);
                        } else if (b2 == 80) {
                            current = (current + 1) % options.size();
                            numInput.setLength(0);
                            redraw(current, ansi);
                        }
                    }
                    continue;
                }

                // ANSI: ESC [ A (Up) / ESC [ B (Down). Zuerst sofortige Bytes aus der Queue holen.
                if (b == 27) {
                    Integer b2 = queue.poll(0, TimeUnit.MILLISECONDS);
                    if (b2 == null) b2 = queue.poll(120, TimeUnit.MILLISECONDS);
                    Integer b3 = null;
                    if (b2 != null && (b2 == 91 || b2 == 79)) {
                        b3 = queue.poll(0, TimeUnit.MILLISECONDS);
                        if (b3 == null) b3 = queue.poll(120, TimeUnit.MILLISECONDS);
                    }
                    if (b3 != null) {
                        if (b3 == 65) {
                            current = (current - 1 + options.size()) % options.size();
                            numInput.setLength(0);
                            redraw(current, ansi);
                        } else if (b3 == 66) {
                            current = (current + 1) % options.size();
                            numInput.setLength(0);
                            redraw(current, ansi);
                        }
                    }
                    continue;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return options.get(0);
        } finally {
            if (ansi) out.print(Ansi.SHOW_CURSOR);
        }
    }

    private String selectWithScanner(int current, boolean ansi) {
        out.print("\nEnter number (1-" + options.size() + ") or Enter for current: ");
        out.flush();
        try (Scanner scanner = new Scanner(in)) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line != null) line = line.trim();
                if (line == null || line.isEmpty()) return options.get(current);
                try {
                    int n = Integer.parseInt(line);
                    if (n >= 1 && n <= options.size()) return options.get(n - 1);
                } catch (NumberFormatException ignored) {}
            }
        }
        return options.get(current);
    }

    private void draw(int current, boolean ansi) {
        if (ansi) out.print(Ansi.BOLD + title + Ansi.RESET + "\n\n");
        else out.println(title + "\n");
        for (int i = 0; i < options.size(); i++) {
            String line = (i + 1) + ". " + options.get(i);
            if (i == current) {
                if (ansi) out.print(Ansi.BOLD + "> " + line + Ansi.RESET + "\n");
                else out.println("> " + line);
            } else {
                if (ansi) out.print("  " + line + "\n");
                else out.println("  " + line);
            }
        }
        out.print("\n(Up/Down: move, Enter: select, or type number): ");
        out.flush();
    }

    private void redraw(int current, boolean ansi) {
        int lines = options.size() + 3;
        if (ansi) {
            for (int i = 0; i < lines; i++) out.print(Ansi.CURSOR_UP);
            for (int i = 0; i < lines; i++) {
                out.print(Ansi.ERASE_LINE);
                if (i < lines - 1) out.print(Ansi.CURSOR_DOWN);
            }
            for (int i = 0; i < lines; i++) out.print(Ansi.CURSOR_UP);
        }
        draw(current, ansi);
    }
}
